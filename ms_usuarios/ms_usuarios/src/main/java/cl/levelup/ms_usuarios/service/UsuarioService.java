package cl.levelup.ms_usuarios.service;

import cl.levelup.ms_usuarios.dto.UsuarioResponse;
import cl.levelup.ms_usuarios.exception.GlobalExceptionHandler.BusinessException;
import cl.levelup.ms_usuarios.exception.GlobalExceptionHandler.ResourceNotFoundException;
import cl.levelup.ms_usuarios.model.Rol;
import cl.levelup.ms_usuarios.model.Usuario;
import cl.levelup.ms_usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public UsuarioResponse obtener(Long id) {
        return toDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")));
    }

    public UsuarioResponse obtenerPorEmail(String email) {
        return toDTO(repository.findByCorreo(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")));
    }

    public UsuarioResponse crear(UsuarioResponse dto) {
        if (repository.existsByCorreo(dto.getEmail())) {
            throw new BusinessException("El correo ya está registrado");
        }
        Usuario u = new Usuario();
        u.setRun(dto.getRun());
        u.setNombre(dto.getNombre());
        u.setApellidos(dto.getApellidos());
        u.setCorreo(dto.getEmail());
        u.setPassword(passwordEncoder.encode("Duoc1234"));
        u.setRol(Rol.valueOf(dto.getRol() != null ? dto.getRol() : "CLIENTE"));
        u.setRegion(dto.getRegion());
        u.setComuna(dto.getComuna());
        u.setDireccion(dto.getDireccion());
        return toDTO(repository.save(u));
    }

    public UsuarioResponse actualizar(Long id, UsuarioResponse dto) {
        Usuario u = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        u.setNombre(dto.getNombre());
        u.setApellidos(dto.getApellidos());
        u.setCorreo(dto.getEmail());
        if (dto.getRol() != null) {
            u.setRol(Rol.valueOf(dto.getRol()));
        }
        return toDTO(repository.save(u));
    }

    public void eliminar(Long id, String currentEmail) {
        Usuario u = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (u.getCorreo().equals(currentEmail)) {
            throw new BusinessException("Un administrador no puede eliminarse a sí mismo");
        }
        repository.delete(u);
    }

    private UsuarioResponse toDTO(Usuario u) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.setId(u.getId());
        dto.setRun(u.getRun());
        dto.setNombre(u.getNombre());
        dto.setApellidos(u.getApellidos());
        dto.setEmail(u.getCorreo());
        dto.setRol(u.getRol().name());
        dto.setRegion(u.getRegion());
        dto.setComuna(u.getComuna());
        dto.setDireccion(u.getDireccion());
        return dto;
    }
}