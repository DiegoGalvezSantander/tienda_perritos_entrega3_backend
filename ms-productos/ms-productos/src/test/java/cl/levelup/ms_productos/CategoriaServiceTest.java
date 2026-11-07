package cl.levelup.ms_productos;

import cl.levelup.ms_productos.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_productos.repository.CategoriaRepository;
import cl.levelup.ms_productos.repository.ProductoRepository;
import cl.levelup.ms_productos.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private ProductoRepository productoRepository;
    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void eliminar_lanzaExcepcion_cuandoTieneProductos() {
        given(categoriaRepository.existsById(1L)).willReturn(true);
        given(productoRepository.existsByCategoriaId(1L)).willReturn(true);

        assertThatThrownBy(() -> categoriaService.eliminar(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("No se puede eliminar una categoría que tiene productos asociados");
    }
}