package org.libreria.gestionstock.controller;

import org.libreria.gestionstock.model.Producto;
import org.libreria.gestionstock.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    // INYECCIÓN: Usamos el Service
    @Autowired
    private ProductoService productoService;

    // ----------------------------------------------------
    // 1. CREAR (POST)
    // Endpoint: POST http://localhost:8080/api/productos
    // ----------------------------------------------------
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto nuevoProducto) {
        // La lógica de negocio se delega al Service
        Producto productoGuardado = productoService.guardarProducto(nuevoProducto);
        return new ResponseEntity<>(productoGuardado, HttpStatus.CREATED);
    }

    // ----------------------------------------------------
    // 2. LEER TODOS (GET)
    // Endpoint: GET http://localhost:8080/api/productos
    // ----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();

        // Manejo de respuesta: 200 OK si hay contenido, 204 No Content si está vacío
        return productos.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // ----------------------------------------------------
    // 3. LEER POR ID (GET/{id})
    // Endpoint: GET http://localhost:8080/api/productos/1
    // ----------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                // Si lo encuentra, devuelve 200 OK y el producto
                .map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
                // Si no lo encuentra, devuelve 404 Not Found (¡Esto resuelve el error 404 anterior!)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ----------------------------------------------------
    // ----------------------------------------------------
    // 4. ACTUALIZAR (PUT)
    // Endpoint: PUT http://localhost:8080/api/productos/{id}
    // ----------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto productoDetalles) {

        // 1. Buscamos el producto existente por el ID
        Optional<Producto> productoExistente = productoService.obtenerPorId(id);

        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();

            // 2. Aquí iría la lógica de actualización:
            // Solo actualizamos los campos que el usuario puede cambiar,
            // como el código, nombre, marca y precio. El stock se maneja aparte.
            producto.setCodigo(productoDetalles.getCodigo());
            producto.setNombre(productoDetalles.getNombre());
            producto.setMarca(productoDetalles.getMarca());
            producto.setPrecioVenta(productoDetalles.getPrecioVenta());

            // 3. Guardamos el producto actualizado (usamos el mismo método guardarProducto)
            Producto productoActualizado = productoService.guardarProducto(producto);

            // Devolvemos 200 OK con el producto actualizado
            return new ResponseEntity<>(productoActualizado, HttpStatus.OK);

        } else {
            // Si el producto no existe, devolvemos 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // ----------------------------------------------------
    // 5. BORRAR (DELETE)
    // Endpoint: DELETE http://localhost:8080/api/productos/{id}
    // ----------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarProducto(@PathVariable Long id) {

        // Verificamos si existe antes de intentar borrar
        if (productoService.obtenerPorId(id).isPresent()) {
            productoService.borrarProducto(id);
            // Devolvemos 204 No Content para indicar que la acción fue exitosa pero no hay cuerpo de respuesta
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Si el producto no existe, devolvemos 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}