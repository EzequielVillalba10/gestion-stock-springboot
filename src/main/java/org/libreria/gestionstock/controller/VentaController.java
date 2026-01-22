package org.libreria.gestionstock.controller;

import org.libreria.gestionstock.model.Stock;
import org.libreria.gestionstock.model.Venta;
import org.libreria.gestionstock.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ventas") // Endpoint base: http://localhost:8080/api/ventas
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // ----------------------------------------------------
    // 1. REGISTRAR VENTA (POST)
    // ----------------------------------------------------
    @PostMapping("/registrar")
    public ResponseEntity<Venta> registrarVenta(@RequestBody Venta venta) {
        // 🕵️ LÍNEA DE DEPURACIÓN:
        System.out.println("DEBUG - Venta recibida: " + venta);
        if (venta.getDetalles() != null) {
            System.out.println("DEBUG - Cantidad de detalles: " + venta.getDetalles().size());
        } else {
            System.out.println("DEBUG - LA LISTA DE DETALLES LLEGÓ NULL");
        }

        Venta ventaRegistrada = ventaService.registrarVenta(venta);
        return new ResponseEntity<>(ventaRegistrada, HttpStatus.CREATED);
    }

    // ----------------------------------------------------
    // 2. LEER TODAS LAS VENTAS (GET)
    // ----------------------------------------------------
    // Opcional: Podrías añadir un método GET para listar ventas, pero nos enfocaremos en la prueba POST.

    // Endpoint: GET http://localhost:8080/api/ventas/reporte/total-hoy
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Venta>> obtenerVentasPorFecha(
            @RequestParam String inicio,
            @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        List<Venta> ventas = ventaService.obtenerVentasPorFecha(fechaInicio, fechaFin);
        return ResponseEntity.ok(ventas);
    }
}