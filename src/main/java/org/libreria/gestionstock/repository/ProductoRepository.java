package org.libreria.gestionstock.repository;

import org.libreria.gestionstock.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    // Spring Data JPA lo implementa automáticamente
    Producto findByCodigo(String codigo);
}
