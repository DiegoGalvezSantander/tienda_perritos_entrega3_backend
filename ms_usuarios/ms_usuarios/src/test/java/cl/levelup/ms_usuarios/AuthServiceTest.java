package cl.levelup.ms_usuarios;

import cl.levelup.ms_usuarios.dto.LoginRequest;
import cl.levelup.ms_usuarios.dto.RegistroRequest;
import cl.levelup.ms_usuarios.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_usuarios.model.Rol;
import cl.levelup.ms_usuarios.model.Usuario;
import cl.levelup.ms_usuarios.repository.UsuarioRepository;
import cl.levelup.ms_usuarios.security.JwtService;
import cl.levelup.ms_usuarios.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    @Test
    void login_lanzaExcepcion_cuandoUsuarioNoExiste() {
        LoginRequest req = new LoginRequest();
        req.setEmail("noexiste@test.cl");
        given(usuarioRepository.findByCorreo("noexiste@test.cl")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Credenciales incorrectas");
    }

    @Test
    void registro_publico_creaClienteYCifraPassword() {
        RegistroRequest req = new RegistroRequest();
        req.setRut("111111111");
        req.setNombre("Test");
        req.setApellidos("Test");
        req.setEmail("test@duoc.cl");
        req.setPassword("Password123");

        given(usuarioRepository.existsByCorreo("test@duoc.cl")).willReturn(false);
        given(usuarioRepository.existsByRun("111111111")).willReturn(false);
        given(passwordEncoder.encode("Password123")).willReturn("hash_encriptado");
        given(usuarioRepository.save(any(Usuario.class))).willAnswer(i -> i.getArgument(0));
        given(jwtService.generateAccessToken(anyString(), anyString())).willReturn("token");

        authService.registro(req);

        verify(usuarioRepository).save(org.mockito.ArgumentMatchers.argThat(u -> 
            u.getRol() == Rol.CLIENTE && 
            u.getPassword().equals("hash_encriptado")
        ));
    }

    @Test
    void refresh_lanzaExcepcion_cuandoSeUsaAccessToken() {
        given(jwtService.isTokenValid("accessToken")).willReturn(true);
        given(jwtService.isRefreshToken("accessToken")).willReturn(false);

        assertThatThrownBy(() -> authService.refresh("accessToken"))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("Refresh token inválido");
    }
}