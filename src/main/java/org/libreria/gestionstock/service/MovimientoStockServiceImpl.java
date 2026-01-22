package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.MovimientoStock;
import org.libreria.gestionstock.repository.MovimientoStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoStockServiceImpl implements MovimientoStockService {

    @Autowired
    private MovimientoStockRepository movimientoRepository;

    @Override
    public List<MovimientoStock> obtenerHistorialPorProducto(Long productoId) {

        return movimientoRepository.findByProductoIdOrderByFechaDesc(productoId);
    }
}