package org.libreria.gestionstock.repository;

import org.libreria.gestionstock.model.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
    List<MovimientoStock> findByProductoIdOrderByFechaDesc(Long productoId);
}