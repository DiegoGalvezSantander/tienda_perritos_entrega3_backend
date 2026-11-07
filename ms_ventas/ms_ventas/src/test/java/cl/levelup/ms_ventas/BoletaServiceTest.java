package cl.levelup.ms_ventas;

import cl.levelup.ms_ventas.config.ProductoClient;
import cl.levelup.ms_ventas.dto.CompraRequest;
import cl.levelup.ms_ventas.dto.ItemCompra;
import cl.levelup.ms_ventas.dto.ProductoDTO;
import cl.levelup.ms_ventas.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_ventas.repository.BoletaRepository;
import cl.levelup.ms_ventas.service.BoletaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoletaServiceTest {
    @Mock
    private BoletaRepository boletaRepository;
    @Mock
    private ProductoClient productoClient;
    @InjectMocks
    private BoletaService boletaService;

    @Test
    void registrarCompra_calculaTotalCorrectamente() {
        ItemCompra item = new ItemCompra();
        item.setProductoId(1L);
        item.setCantidad(2);

        CompraRequest request = new CompraRequest();
        request.setNombre("Juan");
        request.setApellidos("Perez");
        request.setProductos(List.of(item));

        ProductoDTO prod = new ProductoDTO();
        prod.setId(1L);
        prod.setNombre("Rascador");
        prod.setPrecio(new BigDecimal("1000"));
        prod.setPrecioOriginal(new BigDecimal("1500"));
        prod.setEnOferta(true);

        given(productoClient.obtenerProducto(1L)).willReturn(prod);
        given(boletaRepository.save(any())).willAnswer(i -> i.getArgument(0));

        var response = boletaService.registrarCompra(request);

        assertThat(response.getTotal()).isEqualByComparingTo(new BigDecimal("2000"));
        verify(productoClient).descontarStock(request.getProductos());
    }

    @Test
    void registrarCompra_lanzaExcepcion_cuandoNoHayStockSuficiente() {
        ItemCompra item = new ItemCompra();
        item.setProductoId(1L);
        item.setCantidad(10);

        CompraRequest request = new CompraRequest();
        request.setProductos(List.of(item));

        ProductoDTO prod = new ProductoDTO();
        prod.setId(1L);
        prod.setNombre("Rascador");
        prod.setPrecio(new BigDecimal("1000"));

        given(productoClient.obtenerProducto(1L)).willReturn(prod);
        
        doThrow(new RuntimeException("Stock insuficiente")).when(productoClient).descontarStock(any());

        assertThatThrownBy(() -> boletaService.registrarCompra(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Error al procesar la compra");

        verify(boletaRepository, never()).save(any());
        verify(productoClient).devolverStock(request.getProductos());
    }
}