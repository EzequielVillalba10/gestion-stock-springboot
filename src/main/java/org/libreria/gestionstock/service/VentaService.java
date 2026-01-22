package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.Stock;
import org.libreria.gestionstock.model.Venta;
import org.libreria.gestionstock.model.DetalleVenta;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaService {

    // Método clave que registra la venta y descuenta stock
    Venta registrarVenta(Venta venta);

    Double calcularVentasTotalesHoy();

    List<Stock> obtenerStockBajoMinimo();

    // ✅ NUEVOS MÉTODOS:
    List<Venta> obtenerTodasLasVentas();
    List<Venta> obtenerVentasPorFecha(LocalDateTime inicio, LocalDateTime fin);
}