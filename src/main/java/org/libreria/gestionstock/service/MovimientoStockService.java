package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.MovimientoStock;
import java.util.List;

public interface MovimientoStockService {
    List<MovimientoStock> obtenerHistorialPorProducto(Long productoId);
}