package cl.levelup.ms_usuarios.controller;

import cl.levelup.ms_usuarios.dto.AuthResponse;
import cl.levelup.ms_usuarios.dto.LoginRequest;
import cl.levelup.ms_usuarios.dto.RegistroRequest;
import cl.levelup.ms_usuarios.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest dto) {
        return ResponseEntity.ok(service.login(dto));
    }

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@Valid @RequestBody RegistroRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registro(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.substring(7);
        return ResponseEntity.ok(service.refresh(token));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String email = service.obtenerEmailDelToken(jwt);
        return ResponseEntity.ok(service.me(email));
    }
}