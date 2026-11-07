package cl.levelup.ms_productos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String plataforma;
    private Integer stock;
    private Integer stockCritico;

    private BigDecimal precioOferta; 
    private BigDecimal precioBase;   

    @JsonProperty("precio")
    public BigDecimal getPrecio() {
        return (precioOferta != null && precioOferta.compareTo(precioBase) < 0) ? precioOferta : precioBase;
    }

    @JsonProperty("precioOriginal")
    public BigDecimal getPrecioOriginal() {
        return precioBase;
    }

    @JsonProperty("enOferta")
    public boolean isEnOferta() {
        return precioOferta != null && precioOferta.compareTo(precioBase) < 0;
    }

    @JsonProperty("imagen")
    private String imagenUrl;

    @JsonProperty("categoria")
    private String categoriaNombre;

    private Boolean activo;
}