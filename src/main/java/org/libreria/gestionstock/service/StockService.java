package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.Stock;
import org.libreria.gestionstock.model.Producto; // Importamos Producto para buscar

import java.util.List;
import java.util.Optional;

public interface StockService {

    // Método principal para actualizar la cantidad de stock
    Optional<Stock> actualizarCantidad(Long productoId, Integer cantidadAjuste);

    // Método para obtener el stock por ID de Producto
    Optional<Stock> obtenerStockPorProductoId(Long productoId);



    //DELETE
    void delete(Stock entity);

    List<Stock> obtenerStockBajoMinimo();

    Double calcularValorTotalInventario();
}