package cl.levelup.ms_ventas.controller;

import cl.levelup.ms_ventas.dto.BoletaResponse;
import cl.levelup.ms_ventas.dto.CompraRequest;
import cl.levelup.ms_ventas.model.Boleta;
import cl.levelup.ms_ventas.repository.BoletaRepository;
import cl.levelup.ms_ventas.service.BoletaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class BoletaController {
    private final BoletaService service;
    private final BoletaRepository repository;

    public BoletaController(BoletaService service, BoletaRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("/boletas")
    public ResponseEntity<BoletaResponse> registrar(@RequestBody CompraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarCompra(request));
    }

    @GetMapping("/boletas")
    public ResponseEntity<List<Boleta>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/boletas/{id}")
    public ResponseEntity<Boleta> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(repository.findById(id).orElseThrow());
    }

    @GetMapping("/reportes/resumen")
    public ResponseEntity<Map<String, Object>> resumen() {
        var boletas = repository.findAll();
        BigDecimal total = boletas.stream().map(b -> b.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
        long cantidad = boletas.size();
        BigDecimal promedio = cantidad > 0 ? total.divide(new BigDecimal(cantidad), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        Map<String, Object> response = new HashMap<>();
        response.put("totalVendido", total);
        response.put("cantidadBoletas", cantidad);
        response.put("ticketPromedio", promedio);

        return ResponseEntity.ok(response);
    }
}