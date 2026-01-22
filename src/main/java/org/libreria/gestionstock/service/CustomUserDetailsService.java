package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.Usuario;
import org.libreria.gestionstock.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscamos el usuario en nuestra base de datos
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Lo convertimos al formato que Spring Security entiende
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // Esta ya está encriptada en la DB
                .roles(usuario.getRol().replace("ROLE_", "")) // Spring agrega "ROLE_" automáticamente
                .build();
    }
}