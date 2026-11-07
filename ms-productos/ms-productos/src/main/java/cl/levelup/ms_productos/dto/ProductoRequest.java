package cl.levelup.ms_productos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoRequest {
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    private String descripcion;
    private String plataforma;
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precio;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioOferta;
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0)
    private Integer stock;
    @NotNull(message = "El stock crítico es obligatorio")
    @Min(value = 0)
    private Integer stockCritico;
    @NotBlank(message = "La imagen es obligatoria")
    private String imagenUrl;
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
}