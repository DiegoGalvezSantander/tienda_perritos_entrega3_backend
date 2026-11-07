package cl.levelup.ms_productos.repository;

import cl.levelup.ms_productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByCodigo(String codigo);
    boolean existsByCategoriaId(Long categoriaId);
    List<Producto> findByPrecioOfertaIsNotNullAndActivoTrue();
    
    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockCritico AND p.activo = true")
    List<Producto> findCriticos();
}