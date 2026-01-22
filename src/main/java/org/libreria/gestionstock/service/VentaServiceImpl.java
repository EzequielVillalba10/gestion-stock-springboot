package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.DetalleVenta;
import org.libreria.gestionstock.model.Producto;
import org.libreria.gestionstock.model.Stock;
import org.libreria.gestionstock.model.Venta;
import org.libreria.gestionstock.repository.ProductoRepository;
import org.libreria.gestionstock.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // =========================================================
    // 1. REGISTRAR VENTA (POST)
    // =========================================================
    @Override
    @Transactional
    public Venta registrarVenta(Venta venta) {
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new RuntimeException("No se puede registrar una venta sin productos.");
        }

        venta.setFechaVenta(LocalDateTime.now());

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalle.getProducto().getId()));

            Stock stock = producto.getStock();
            if (stock == null) {
                throw new RuntimeException("El producto " + producto.getNombre() + " no tiene un registro de stock.");
            }

            if (stock.getCantidad() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre() +
                        " (Disponible: " + stock.getCantidad() + ")");
            }

            // Descontar Stock
            stock.setCantidad(stock.getCantidad() - detalle.getCantidad());

            // Completar datos del detalle
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setPrecioUnitarioVenta(producto.getPrecioVenta());
        }

        return ventaRepository.save(venta);
    }

    // =========================================================
    // 2. REPORTES Y LECTURA
    // =========================================================

    @Override
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }


    @Override
    public List<Venta> obtenerVentasPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }


    /**
     * Calcula la suma total de dinero vendido en el día de hoy.
     * Consulta todas las ventas de hoy y suma (cantidad * precio_unitario) de cada detalle.
     */
    @Override
    public Double calcularVentasTotalesHoy() {
        // Rango de tiempo: Desde 00:00:00 hasta 23:59:59 de hoy
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // 🚨 NOTA: ESTA LÍNEA NECESITA QUE TENGAS EL MÉTODO findByFechaVentaBetween en VentaRepository
        List<Venta> ventasHoy = ventaRepository.findByFechaVentaBetween(inicioDia, finDia);

        return ventasHoy.stream()
                .flatMap(venta -> venta.getDetalles().stream())
                .mapToDouble(detalle -> detalle.getPrecioUnitarioVenta() * detalle.getCantidad())
                .sum();
    }

    /**
     * Devuelve una lista de los objetos Stock cuyo nivel de cantidad es menor o igual al stock mínimo.
     */
    @Override
    public List<Stock> obtenerStockBajoMinimo() {
        return productoRepository.findAll().stream()
                .map(Producto::getStock)
                .filter(stock -> stock.getCantidad() <= stock.getStockMinimo()) // 👈 CORREGIDO AQUÍ
                .collect(Collectors.toList());
    }
}