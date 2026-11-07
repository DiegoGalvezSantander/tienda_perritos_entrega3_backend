package cl.levelup.ms_usuarios.service;

import cl.levelup.ms_usuarios.dto.AuthResponse;
import cl.levelup.ms_usuarios.dto.LoginRequest;
import cl.levelup.ms_usuarios.dto.RegistroRequest;
import cl.levelup.ms_usuarios.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_usuarios.model.Rol;
import cl.levelup.ms_usuarios.model.Usuario;
import cl.levelup.ms_usuarios.repository.UsuarioRepository;
import cl.levelup.ms_usuarios.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest dto) {
        Usuario usuario = usuarioRepository.findByCorreo(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciales incorrectas"));
        
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new BusinessException("Credenciales incorrectas");
        }

        return buildAuthResponse(usuario);
    }

    public AuthResponse registro(RegistroRequest dto) {
        if (!esRutValido(dto.getRut())) {
            throw new BusinessException("El RUN ingresado no es válido");
        }
        if (usuarioRepository.existsByCorreo(dto.getEmail())) {
            throw new BusinessException("El correo ya está registrado");
        }
        if (usuarioRepository.existsByRun(dto.getRut())) {
            throw new BusinessException("El RUN ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setRun(dto.getRut());
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreo(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(Rol.CLIENTE);
        usuario.setRegion(dto.getRegion());
        usuario.setComuna(dto.getComuna());
        usuario.setDireccion(dto.getDireccion());

        usuarioRepository.save(usuario);
        return buildAuthResponse(usuario);
    }

    public AuthResponse refresh(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken) || !jwtService.isRefreshToken(refreshToken)) {
            throw new BusinessException("Refresh token inválido");
        }
        String email = jwtService.extractUsername(refreshToken);
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
        return buildAuthResponse(usuario);
    }

    public AuthResponse me(String email) {
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
        return buildAuthResponse(usuario);
    }

    public String obtenerEmailDelToken(String token) {
        return jwtService.extractUsername(token);
    }

    private AuthResponse buildAuthResponse(Usuario usuario) {
        AuthResponse response = new AuthResponse();
        response.setEmail(usuario.getCorreo());
        response.setNombre(usuario.getNombre());
        response.setTipo(usuario.getRol().name());
        response.setAccessToken(jwtService.generateAccessToken(usuario.getCorreo(), usuario.getRol().name()));
        response.setRefreshToken(jwtService.generateRefreshToken(usuario.getCorreo()));
        return response;
    }

    private boolean esRutValido(String rut) {
        if (rut == null || rut.isEmpty()) return false;
        rut = rut.replace(".", "").replace("-", "").trim();
        if (rut.length() < 2) return false;
        try {
            int cuerpo = Integer.parseInt(rut.substring(0, rut.length() - 1));
            char dv = rut.charAt(rut.length() - 1);
            int m = 0, s = 1;
            for (; cuerpo != 0; cuerpo /= 10) {
                s = (s + cuerpo % 10 * (9 - m++ % 6)) % 11;
            }
            char dvEsperado = (char) (s != 0 ? s + 47 : 75);
            return dv == dvEsperado;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}