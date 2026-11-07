package cl.levelup.ms_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistroRequest {
    @NotBlank
    private String rut;
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellidos;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String password;
    private String region;
    private String comuna;
    private String direccion;
}