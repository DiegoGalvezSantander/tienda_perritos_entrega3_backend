package cl.levelup.ms_usuarios.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    
    @JsonProperty("rut")
    private String run;
    
    private String nombre;
    private String apellidos;
    private String email;
    
    @JsonProperty("tipo")
    private String rol;
    
    private String region;
    private String comuna;
    private String direccion;
}