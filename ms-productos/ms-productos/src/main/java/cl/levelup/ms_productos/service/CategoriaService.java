package cl.levelup.ms_productos.service;

import cl.levelup.ms_productos.dto.CategoriaRequest;
import cl.levelup.ms_productos.dto.CategoriaResponse;
import cl.levelup.ms_productos.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_productos.exception.GlobalExceptionHandler.ResourceNotFoundException;
import cl.levelup.ms_productos.model.Categoria;
import cl.levelup.ms_productos.repository.CategoriaRepository;
import cl.levelup.ms_productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CategoriaResponse obtener(Long id) {
        return toDTO(categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada")));
    }

    public CategoriaResponse crear(CategoriaRequest dto) {
        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            throw new BusinessException("Ya existe una categoría con ese nombre.");
        }
        Categoria cat = new Categoria();
        mapear(cat, dto);
        return toDTO(categoriaRepository.save(cat));
    }

    public CategoriaResponse actualizar(Long id, CategoriaRequest dto) {
        Categoria cat = categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        if (!cat.getNombre().equals(dto.getNombre()) && categoriaRepository.existsByNombre(dto.getNombre())) {
            throw new BusinessException("Ya existe una categoría con ese nombre.");
        }
        mapear(cat, dto);
        return toDTO(categoriaRepository.save(cat));
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada.");
        }
        if (productoRepository.existsByCategoriaId(id)) {
            throw new BusinessException("No se puede eliminar una categoría que tiene productos asociados.");
        }
        categoriaRepository.deleteById(id);
    }

    private void mapear(Categoria cat, CategoriaRequest dto) {
        cat.setNombre(dto.getNombre());
        cat.setDescripcion(dto.getDescripcion());
        cat.setColor(dto.getColor());
    }

    private CategoriaResponse toDTO(Categoria cat) {
        return new CategoriaResponse(cat.getId(), cat.getNombre(), cat.getDescripcion(), cat.getColor());
    }
}