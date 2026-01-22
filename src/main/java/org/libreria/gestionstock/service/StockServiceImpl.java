package org.libreria.gestionstock.service;

import org.libreria.gestionstock.exception.StockInsuficienteException;
import org.libreria.gestionstock.model.MovimientoStock;
import org.libreria.gestionstock.model.Stock;
import org.libreria.gestionstock.repository.MovimientoStockRepository;
import org.libreria.gestionstock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importamos para manejo de transacciones

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private MovimientoStockRepository movimientoRepository; // <<-- MODIFICACIÓN 1: INYECCIÓN DEL REPOSITORIO

    @Override
    public Optional<Stock> obtenerStockPorProductoId(Long productoId) {
        return Optional.ofNullable(stockRepository.findByProductoId(productoId));
    }

    // Usamos @Transactional para asegurar que la lectura y escritura sea atómica
    @Override
    @Transactional
    public Optional<Stock> actualizarCantidad(Long productoId, Integer cantidadAjuste) {

        Optional<Stock> stockOpt = obtenerStockPorProductoId(productoId);

        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();

            Integer cantidadAnterior = stock.getCantidad();
            Integer nuevaCantidad = stock.getCantidad() + cantidadAjuste;

            // 🚨 CAMBIO CLAVE: LANZAR LA EXCEPCIÓN
            if (nuevaCantidad < 0) {
                // Esto detiene la transacción (@Transactional) y le informa al cliente.
                throw new StockInsuficienteException("No hay suficiente stock para el producto: " + stock.getProducto().getNombre() + ". Stock actual: " + stock.getCantidad());
            }

            registrarMovimiento(stock, cantidadAnterior, cantidadAjuste, nuevaCantidad);

            stock.setCantidad(nuevaCantidad);
            return Optional.of(stockRepository.save(stock));

        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Stock entity) {
        stockRepository.delete(entity);
    }

    // -----------------------------------------------------------------
    // NUEVO MÉTODO: Lógica para Reporte de Stock Mínimo
    // -----------------------------------------------------------------
    @Override
    public List<Stock> obtenerStockBajoMinimo() {

        // El StockRepository ya tiene todos los métodos CRUD para Stock
        // Simplemente obtenemos todos los registros de stock y aplicamos el filtro en Java.

        return stockRepository.findAll().stream()
                // Filtramos aquellos cuya cantidad sea menor o igual al stock mínimo
                .filter(stock -> stock.getCantidad() <= stock.getStockMinimo())
                .toList();
    }

    // -----------------------------------------------------------------
    // NUEVO MÉTODO PRIVADO PARA GESTIONAR EL LOG
    // -----------------------------------------------------------------
    private void registrarMovimiento(Stock stock, Integer cantidadAnterior, Integer diferencia, Integer nuevaCantidad) {

        MovimientoStock mov = new MovimientoStock();
        mov.setProductoId(stock.getProducto().getId());
        mov.setProductoNombre(stock.getProducto().getNombre());
        mov.setCantidadAnterior(cantidadAnterior);
        mov.setCantidadNueva(nuevaCantidad);
        mov.setDiferencia(diferencia);

        // Asignación de tipo de operación basado en el signo
        if (diferencia < 0) {
            mov.setTipoOperacion("VENTA_AUTOMATICA");
        } else if (diferencia > 0) {
            mov.setTipoOperacion("COMPRA_O_AJUSTE_POSITIVO");
        } else {
            mov.setTipoOperacion("SIN_CAMBIO");
        }

        movimientoRepository.save(mov);
    }


    @Override
    public Double calcularValorTotalInventario() {
        return stockRepository.findAll().stream()
                .mapToDouble(stock -> {
                    // Obtenemos el precio del producto
                    Double precio = stock.getProducto().getPrecioVenta();

                    // Si el precio es null, lo tratamos como 0.0 para que no falle la multiplicación
                    double precioSeguro = (precio != null) ? precio : 0.0;

                    return stock.getCantidad() * precioSeguro;
                })
                .sum();
    }





}