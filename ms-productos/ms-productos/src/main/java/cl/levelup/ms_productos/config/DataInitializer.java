package cl.levelup.ms_productos.config;

import cl.levelup.ms_productos.model.Categoria;
import cl.levelup.ms_productos.model.Producto;
import cl.levelup.ms_productos.repository.CategoriaRepository;
import cl.levelup.ms_productos.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CategoriaRepository categoriaRepo;
    private final ProductoRepository productoRepo;

    public DataInitializer(CategoriaRepository categoriaRepo, ProductoRepository productoRepo) {
        this.categoriaRepo = categoriaRepo;
        this.productoRepo = productoRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoriaRepo.count() == 0) {
            Categoria alimentoSeco = categoriaRepo.save(new Categoria(null, "Alimento Seco", "Comida seca", "#FF0000"));
            Categoria alimentoHumedo = categoriaRepo.save(new Categoria(null, "Alimento Húmedo", "Comida húmeda", "#00FF00"));
            Categoria juguetes = categoriaRepo.save(new Categoria(null, "Juguetes", "Juguetes para mascotas", "#0000FF"));
            Categoria rascadores = categoriaRepo.save(new Categoria(null, "Rascadores y Gimnasios", "Rascadores para gatos", "#FFFF00"));
            Categoria correas = categoriaRepo.save(new Categoria(null, "Correas, Collares y Arneses", "Accesorios", "#FF00FF"));

            productoRepo.save(new Producto(null, "PROD001", "Rascador Premium", "Rascador resistente para gatos.", "Gatos",
                    new BigDecimal("49990"), new BigDecimal("39990"), 15, 5, "IMG/ProductosGato/SuperRascadorGato.png", rascadores, true));

            productoRepo.save(new Producto(null, "PROD002", "Cerdito Oink", "Juguete divertido para perros.", "Perros",
                    new BigDecimal("3990"), null, 30, 5, "IMG/ProductosPerro/CerditoOinkOink.png", juguetes, true));

            productoRepo.save(new Producto(null, "PROD003", "Arnés para Gato", "Arnés ajustable y cómodo.", "Gatos",
                    new BigDecimal("12990"), new BigDecimal("9990"), 10, 3, "IMG/ProductosGato/ArnesAjustableGato.png", correas, true));

            productoRepo.save(new Producto(null, "PROD004", "Sobre Pedigree", "Alimento húmedo para perros.", "Perros",
                    new BigDecimal("800"), null, 50, 10, "IMG/ProductosPerro/SobreCarnePedigreeAdulto.png", alimentoHumedo, true));
        }
    }
}