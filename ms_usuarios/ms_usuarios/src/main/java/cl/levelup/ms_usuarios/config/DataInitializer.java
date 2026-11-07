package cl.levelup.ms_usuarios.config;

import cl.levelup.ms_usuarios.model.Rol;
import cl.levelup.ms_usuarios.model.Usuario;
import cl.levelup.ms_usuarios.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            repository.save(new Usuario(null, "111111111", "Admin", "Sistema", "admin@duoc.cl", passwordEncoder.encode("Admin1234"), Rol.ADMIN, "1", "Santiago", "Casa Matriz"));
            repository.save(new Usuario(null, "222222222", "Vendedor", "Prueba", "vendedor@duoc.cl", passwordEncoder.encode("Vende1234"), Rol.VENDEDOR, "1", "Santiago", "Sucursal 1"));
            repository.save(new Usuario(null, "333333333", "Cliente", "Prueba", "cliente@gmail.com", passwordEncoder.encode("Cliente1234"), Rol.CLIENTE, "1", "Santiago", "Calle Falsa 123"));
        }
    }
}