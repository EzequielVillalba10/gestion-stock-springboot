package org.libreria.gestionstock.controller;

import org.libreria.gestionstock.model.MovimientoStock;
import org.libreria.gestionstock.service.MovimientoStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoStockController {

    @Autowired
    private MovimientoStockService movimientoService;

    // Endpoint: GET /api/movimientos/{productoId}
    @GetMapping("/{productoId}")
    public ResponseEntity<List<MovimientoStock>> obtenerHistorial(
            @PathVariable Long productoId) {

        List<MovimientoStock> historial = movimientoService.obtenerHistorialPorProducto(productoId);

        return ResponseEntity.ok(historial);
    }
}