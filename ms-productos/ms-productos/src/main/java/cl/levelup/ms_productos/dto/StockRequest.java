package cl.levelup.ms_productos.dto;

import lombok.Data;

@Data
public class StockRequest {
    private Long productoId;
    private Integer cantidad;
}