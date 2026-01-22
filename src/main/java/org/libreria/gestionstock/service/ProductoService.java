package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {

    // Contrato para Crear y Actualizar
    Producto guardarProducto(Producto producto);

    // Contratos para Leer (R)
    List<Producto> obtenerTodos();
    Optional<Producto> obtenerPorId(Long id);

    // Contrato para Borrar
    void borrarProducto(Long id);
}