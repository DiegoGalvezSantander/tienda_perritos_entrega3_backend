package cl.levelup.ms_ventas.repository;

import cl.levelup.ms_ventas.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
}