package cl.levelup.ms_ventas.dto;

import lombok.Data;
import java.util.List;

@Data
public class CompraRequest {
    private String nombre;
    private String apellidos;
    private String email;
    private String calle;
    private String departamento;
    private String region;
    private String comuna;
    private String indicaciones;
    private List<ItemCompra> productos;
}