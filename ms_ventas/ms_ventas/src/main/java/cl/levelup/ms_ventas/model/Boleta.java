package cl.levelup.ms_ventas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "boleta")
@Data
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    @Enumerated(EnumType.STRING)
    private EstadoBoleta estado;
    private BigDecimal total;
    private Long usuarioId;
    private String nombreCliente;
    private String apellidosCliente;
    private String correoCliente;
    private String calle;
    private String departamento;
    private String region;
    private String comuna;
    private String indicaciones;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "boleta_id")
    private List<DetalleBoleta> detalles;
}