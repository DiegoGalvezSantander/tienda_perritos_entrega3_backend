package cl.levelup.ms_ventas.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private BigDecimal precioOriginal;
    private boolean enOferta;
}