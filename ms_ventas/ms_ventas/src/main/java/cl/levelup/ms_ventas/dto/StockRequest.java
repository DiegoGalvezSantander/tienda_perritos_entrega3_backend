package cl.levelup.ms_ventas.dto;

import lombok.Data;

@Data
public class StockRequest {
    private Long productoId;
    private Integer cantidad;
}