package cl.levelup.ms_usuarios.controller;

import cl.levelup.ms_usuarios.dto.AuthResponse;
import cl.levelup.ms_usuarios.dto.UsuarioResponse;
import cl.levelup.ms_usuarios.service.AuthService;
import cl.levelup.ms_usuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfil")
public class PerfilController {
    private final AuthService authService;
    private final UsuarioService usuarioService;

    public PerfilController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<AuthResponse> obtener() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(authService.me(email));
    }

    @PutMapping
    public ResponseEntity<UsuarioResponse> actualizar(@RequestBody UsuarioResponse dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioResponse usuario = usuarioService.obtenerPorEmail(email);
        return ResponseEntity.ok(usuarioService.actualizar(usuario.getId(), dto));
    }
}