package org.libreria.gestionstock.service;

import org.libreria.gestionstock.model.Usuario;
import org.libreria.gestionstock.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        // 🔐 IMPORTANTE: Encriptamos la clave antes de enviarla a la base de datos
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);

        return usuarioRepository.save(usuario);
    }
}