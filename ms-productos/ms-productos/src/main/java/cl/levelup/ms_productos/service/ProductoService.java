package cl.levelup.ms_productos.service;

import cl.levelup.ms_productos.dto.ProductoRequest;
import cl.levelup.ms_productos.dto.ProductoResponse;
import cl.levelup.ms_productos.dto.StockRequest;
import cl.levelup.ms_productos.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_productos.exception.GlobalExceptionHandler.ResourceNotFoundException;
import cl.levelup.ms_productos.model.Categoria;
import cl.levelup.ms_productos.model.Producto;
import cl.levelup.ms_productos.repository.CategoriaRepository;
import cl.levelup.ms_productos.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoResponse> listar(String categoriaId, String busqueda) {
        return productoRepository.findAll().stream()
                .filter(Producto::getActivo)
                .filter(p -> categoriaId == null || p.getCategoria().getId().toString().equals(categoriaId))
                .filter(p -> busqueda == null || p.getNombre().toLowerCase().contains(busqueda.toLowerCase()))
                .map(this::toDTO).collect(Collectors.toList());
    }

    public ProductoResponse obtener(Long id) {
        return toDTO(productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado.")));
    }

    public List<ProductoResponse> ofertas() {
        return productoRepository.findByPrecioOfertaIsNotNullAndActivoTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductoResponse> criticos() {
        return productoRepository.findCriticos().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest dto) {
        if (productoRepository.existsByCodigo(dto.getCodigo())) {
            throw new BusinessException("Ya existe un producto con ese código.");
        }
        validarPrecioOferta(dto);
        Categoria cat = categoriaRepository.findById(dto.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));
        Producto p = new Producto();
        mapear(p, dto, cat);
        return toDTO(productoRepository.save(p));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest dto) {
        Producto p = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));
        if (!p.getCodigo().equals(dto.getCodigo()) && productoRepository.existsByCodigo(dto.getCodigo())) {
            throw new BusinessException("Ya existe un producto con ese código.");
        }
        validarPrecioOferta(dto);
        Categoria cat = categoriaRepository.findById(dto.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));
        mapear(p, dto, cat);
        return toDTO(productoRepository.save(p));
    }

    public void eliminar(Long id) {
        Producto p = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));
        p.setActivo(false);
        productoRepository.save(p);
    }

    @Transactional
    public void descontarStock(List<StockRequest> solicitudes) {
        for (StockRequest req : solicitudes) {
            Producto p = productoRepository.findById(req.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + req.getProductoId()));
            if (p.getStock() < req.getCantidad()) {
                throw new BusinessException("Stock insuficiente para el producto: " + p.getNombre());
            }
        }
        for (StockRequest req : solicitudes) {
            Producto p = productoRepository.findById(req.getProductoId()).get();
            p.setStock(p.getStock() - req.getCantidad());
            productoRepository.save(p);
        }
    }

    @Transactional
    public void devolverStock(List<StockRequest> solicitudes) {
        for (StockRequest req : solicitudes) {
            Producto p = productoRepository.findById(req.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + req.getProductoId()));
            p.setStock(p.getStock() + req.getCantidad());
            productoRepository.save(p);
        }
    }

    private void validarPrecioOferta(ProductoRequest dto) {
        if (dto.getPrecioOferta() != null && dto.getPrecioOferta().compareTo(dto.getPrecio()) >= 0) {
            throw new BusinessException("El precio de oferta debe ser menor al precio normal.");
        }
    }

    private void mapear(Producto p, ProductoRequest dto, Categoria cat) {
        p.setCodigo(dto.getCodigo());
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPlataforma(dto.getPlataforma());
        p.setPrecio(dto.getPrecio());
        p.setPrecioOferta(dto.getPrecioOferta());
        p.setStock(dto.getStock());
        p.setStockCritico(dto.getStockCritico());
        p.setImagenUrl(dto.getImagenUrl());
        p.setCategoria(cat);
        p.setActivo(true);
    }

    private ProductoResponse toDTO(Producto p) {
        ProductoResponse dto = new ProductoResponse();
        dto.setId(p.getId());
        dto.setCodigo(p.getCodigo());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPlataforma(p.getPlataforma());
        dto.setStock(p.getStock());
        dto.setStockCritico(p.getStockCritico());
        dto.setActivo(p.getActivo());
        
        dto.setPrecioBase(p.getPrecio());
        dto.setPrecioOferta(p.getPrecioOferta());
        
        dto.setImagenUrl(p.getImagenUrl());
        dto.setCategoriaNombre(p.getCategoria().getNombre());
        return dto;
    }
}