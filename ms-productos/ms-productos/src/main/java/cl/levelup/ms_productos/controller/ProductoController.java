package cl.levelup.ms_productos.controller;

import cl.levelup.ms_productos.dto.ProductoRequest;
import cl.levelup.ms_productos.dto.ProductoResponse;
import cl.levelup.ms_productos.dto.StockRequest;
import cl.levelup.ms_productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar(
            @RequestParam(required = false) String categoriaId,
            @RequestParam(required = false) String busqueda) {
        return ResponseEntity.ok(service.listar(categoriaId, busqueda));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @GetMapping("/ofertas")
    public ResponseEntity<List<ProductoResponse>> ofertas() {
        return ResponseEntity.ok(service.ofertas());
    }

    @GetMapping("/criticos")
    public ResponseEntity<List<ProductoResponse>> criticos() {
        return ResponseEntity.ok(service.criticos());
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stock/descontar")
    public ResponseEntity<Void> descontar(@RequestBody List<StockRequest> solicitudes) {
        service.descontarStock(solicitudes);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stock/devolver")
    public ResponseEntity<Void> devolver(@RequestBody List<StockRequest> solicitudes) {
        service.devolverStock(solicitudes);
        return ResponseEntity.ok().build();
    }
}