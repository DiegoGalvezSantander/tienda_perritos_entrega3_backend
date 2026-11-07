package cl.levelup.ms_ventas.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BoletaResponse {
    private Long id;
    private BigDecimal total;
    private String fecha;
    private String cliente;
}