package org.libreria.gestionstock.repository;

import org.libreria.gestionstock.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
}