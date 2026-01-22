package org.libreria.gestionstock.repository;

import org.libreria.gestionstock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Método para buscar el stock por el ID de su producto asociado
    Stock findByProductoId(Long productoId);

    // NUEVO: Encontrar registros donde la cantidad sea menor que el stock mínimo
    List<Stock> findByCantidadLessThan(Integer cantidad);
}