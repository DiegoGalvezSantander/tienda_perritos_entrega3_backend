package cl.levelup.ms_ventas.config;

import cl.levelup.ms_ventas.dto.ItemCompra;
import cl.levelup.ms_ventas.dto.ProductoDTO;
import cl.levelup.ms_ventas.dto.StockRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductoClient {
    private final RestClient restClient;

    public ProductoClient(@Value("${ms.productos.url}") String url) {
        this.restClient = RestClient.builder().baseUrl(url).build();
    }

    public ProductoDTO obtenerProducto(Long id) {
        return restClient.get().uri("/api/v1/productos/{id}", id).retrieve().body(ProductoDTO.class);
    }

    public void descontarStock(List<ItemCompra> items) {
        List<StockRequest> solicitudes = items.stream().map(i -> {
            StockRequest r = new StockRequest();
            r.setProductoId(i.getProductoId());
            r.setCantidad(i.getCantidad());
            return r;
        }).collect(Collectors.toList());
        restClient.post().uri("/api/v1/productos/stock/descontar").body(solicitudes).retrieve().toBodilessEntity();
    }

    public void devolverStock(List<ItemCompra> items) {
        List<StockRequest> solicitudes = items.stream().map(i -> {
            StockRequest r = new StockRequest();
            r.setProductoId(i.getProductoId());
            r.setCantidad(i.getCantidad());
            return r;
        }).collect(Collectors.toList());
        restClient.post().uri("/api/v1/productos/stock/devolver").body(solicitudes).retrieve().toBodilessEntity();
    }
}