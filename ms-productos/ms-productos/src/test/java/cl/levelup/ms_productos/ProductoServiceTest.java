package cl.levelup.ms_productos;

import cl.levelup.ms_productos.dto.ProductoRequest;
import cl.levelup.ms_productos.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_productos.repository.CategoriaRepository;
import cl.levelup.ms_productos.repository.ProductoRepository;
import cl.levelup.ms_productos.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @InjectMocks
    private ProductoService productoService;

    @Test
    void crear_lanzaExcepcion_cuandoCodigoYaExiste() {
        ProductoRequest dto = new ProductoRequest();
        dto.setCodigo("PROD001");
        given(productoRepository.existsByCodigo("PROD001")).willReturn(true);

        assertThatThrownBy(() -> productoService.crear(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Ya existe un producto con ese código");
    }

    @Test
    void crear_lanzaExcepcion_cuandoPrecioOfertaEsMayorAlPrecio() {
        ProductoRequest dto = new ProductoRequest();
        dto.setCodigo("PROD002");
        dto.setPrecio(new BigDecimal("1000"));
        dto.setPrecioOferta(new BigDecimal("2000"));
        given(productoRepository.existsByCodigo("PROD002")).willReturn(false);

        assertThatThrownBy(() -> productoService.crear(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("El precio de oferta debe ser menor al precio normal");
    }
}