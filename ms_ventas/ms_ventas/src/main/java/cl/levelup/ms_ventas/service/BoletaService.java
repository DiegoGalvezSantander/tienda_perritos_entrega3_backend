package cl.levelup.ms_ventas.service;

import cl.levelup.ms_ventas.config.ProductoClient;
import cl.levelup.ms_ventas.dto.BoletaResponse;
import cl.levelup.ms_ventas.dto.CompraRequest;
import cl.levelup.ms_ventas.dto.ItemCompra;
import cl.levelup.ms_ventas.dto.ProductoDTO;
import cl.levelup.ms_ventas.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_ventas.model.Boleta;
import cl.levelup.ms_ventas.model.DetalleBoleta;
import cl.levelup.ms_ventas.model.EstadoBoleta;
import cl.levelup.ms_ventas.repository.BoletaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoletaService {
    private final BoletaRepository boletaRepository;
    private final ProductoClient productoClient;

    public BoletaService(BoletaRepository boletaRepository, ProductoClient productoClient) {
        this.boletaRepository = boletaRepository;
        this.productoClient = productoClient;
    }

    @Transactional
    public BoletaResponse registrarCompra(CompraRequest request) {
        Boleta boleta = new Boleta();
        boleta.setFecha(LocalDateTime.now());
        boleta.setEstado(EstadoBoleta.PAGADA);
        boleta.setNombreCliente(request.getNombre());
        boleta.setApellidosCliente(request.getApellidos());
        boleta.setCorreoCliente(request.getEmail());
        boleta.setCalle(request.getCalle());
        boleta.setDepartamento(request.getDepartamento());
        boleta.setRegion(request.getRegion());
        boleta.setComuna(request.getComuna());
        boleta.setIndicaciones(request.getIndicaciones());

        List<DetalleBoleta> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemCompra item : request.getProductos()) {
            ProductoDTO prod = productoClient.obtenerProducto(item.getProductoId());
            
            BigDecimal precioReal = prod.getPrecio();
            
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setProductoId(prod.getId());
            detalle.setNombreProducto(prod.getNombre());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioReal);
            detalle.setSubtotal(precioReal.multiply(new BigDecimal(item.getCantidad())));
            
            detalles.add(detalle);
            total = total.add(detalle.getSubtotal());
        }
        
        boleta.setTotal(total);
        boleta.setDetalles(detalles);

        try {
            productoClient.descontarStock(request.getProductos());
            Boleta guardada = boletaRepository.save(boleta);
            
            BoletaResponse response = new BoletaResponse();
            response.setId(guardada.getId());
            response.setTotal(guardada.getTotal());
            response.setFecha(guardada.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            response.setCliente(guardada.getNombreCliente() + " " + guardada.getApellidosCliente());
            return response;
            
        } catch (Exception e) {
            productoClient.devolverStock(request.getProductos());
            throw new BusinessException("Error al procesar la compra (Stock insuficiente o servicio no disponible)");
        }
    }
}