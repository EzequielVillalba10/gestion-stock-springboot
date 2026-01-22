package org.libreria.gestionstock.controller;

import org.libreria.gestionstock.model.Usuario;
import org.libreria.gestionstock.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint: POST http://localhost:8080/api/usuarios/registro
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        // El servicio se encargará de encriptar la contraseña antes de guardar
        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> obtenerUsuarioActual(Principal principal) {
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();

        Map<String, String> response = new HashMap<>();
        response.put("username", userDetails.getUsername());
        response.put("rol", rol);

        return ResponseEntity.ok(response);
    }
}