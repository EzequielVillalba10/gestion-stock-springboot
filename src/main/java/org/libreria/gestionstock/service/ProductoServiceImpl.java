package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.Producto;
import org.libreria.gestionstock.model.Stock;
import org.libreria.gestionstock.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como el componente de servicio
public class ProductoServiceImpl implements ProductoService {

    // Inyectamos el Repositorio para acceder a la DB
    @Autowired
    private ProductoRepository productoRepository;

    // Lógica para guardar/crear
    @Override
    public Producto guardarProducto(Producto producto) {
        // Lógica de negocio: Si el producto es nuevo, inicializamos su stock.
        if (producto.getId() == null) {
            Stock stockInicial = new Stock();
            stockInicial.setCantidad(0);
            stockInicial.setStockMinimo(5); // Define stock mínimo por defecto
            stockInicial.setProducto(producto);
            producto.setStock(stockInicial);
        }

        // El repositorio se encarga de guardar en MySQL
        return productoRepository.save(producto);
    }

    // Lógica para LEER TODOS (R)
    @Override
    public List<Producto> obtenerTodos() {
        // Simplemente delegamos la búsqueda al repositorio
        return productoRepository.findAll();
    }

    // Lógica para LEER POR ID (R)
    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Lógica para BORRAR
    @Override
    public void borrarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}