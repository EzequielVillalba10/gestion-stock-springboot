package org.libreria.gestionstock.controller;

import org.libreria.gestionstock.model.Stock;
import org.libreria.gestionstock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock") // Endpoint base: http://localhost:8080/api/stock
public class StockController {

    @Autowired
    private StockService stockService;

    // ----------------------------------------------------
    // 1. OBTENER STOCK (READ)
    // Endpoint: GET http://localhost:8080/api/stock/{productoId}
    // ----------------------------------------------------
    @GetMapping("/{productoId}")
    public ResponseEntity<Stock> obtenerStock(@PathVariable Long productoId) {
        return stockService.obtenerStockPorProductoId(productoId)
                .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ----------------------------------------------------
    // 2. AÑADIR/VENDER (AJUSTE) (UPDATE)
    // Endpoint: PUT http://localhost:8080/api/stock/{productoId}
    // El Body solo contiene la cantidad de ajuste
    // ----------------------------------------------------
    @PutMapping("/{productoId}")
    public ResponseEntity<Stock> ajustarStock(@PathVariable Long productoId, @RequestBody Integer cantidadAjuste) {

        Optional<Stock> stockOpt = stockService.actualizarCantidad(productoId, cantidadAjuste);

        if (stockOpt.isPresent()) {
            return new ResponseEntity<>(stockOpt.get(), HttpStatus.OK);
        } else {
            // Podría ser 404 (producto no encontrado) o 400 (stock negativo)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> eliminarStockPorProducto(@PathVariable Long productoId) {

        Optional<Stock> stockOpt = stockService.obtenerStockPorProductoId(productoId);

        if (stockOpt.isPresent()) {
            stockService.delete(stockOpt.get()); // Usa el método delete del Service
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/reporte/bajo-minimo")
    public ResponseEntity<List<Stock>> obtenerReporteBajoMinimo() {
        List<Stock> productosBajoMinimo = stockService.obtenerStockBajoMinimo();

        return productosBajoMinimo.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(productosBajoMinimo, HttpStatus.OK);
    }

    // Endpoint: GET http://localhost:8080/api/stock/valor-total
    @GetMapping("/valor-total")
    public ResponseEntity<Double> obtenerValorTotalInventario() {
        Double valorTotal = stockService.calcularValorTotalInventario();
        return ResponseEntity.ok(valorTotal);
    }
}