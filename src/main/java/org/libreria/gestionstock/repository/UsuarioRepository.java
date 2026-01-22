package org.libreria.gestionstock.repository;

import org.libreria.gestionstock.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Este método lo usará Spring Security para el login después
    Optional<Usuario> findByUsername(String username);
}