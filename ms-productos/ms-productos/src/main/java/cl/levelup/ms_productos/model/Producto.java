package cl.levelup.ms_productos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String codigo;
    @Column(nullable = false)
    private String nombre;
    @Column(length = 1000)
    private String descripcion;
    private String plataforma;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;
    @Column(precision = 12, scale = 2)
    private BigDecimal precioOferta;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Integer stockCritico;
    @Column(length = 1000)
    private String imagenUrl;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    @Column(nullable = false)
    private Boolean activo = true;
}