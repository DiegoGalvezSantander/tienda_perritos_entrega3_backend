package cl.levelup.ms_usuarios.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String email;
    private String nombre;
    private String tipo;
    private String accessToken;
    private String refreshToken;
}