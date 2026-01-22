package org.libreria.gestionstock.controller;

import org.libreria.gestionstock.exception.ProductoNoEncontradoException;
import org.libreria.gestionstock.exception.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Le dice a Spring que esta clase maneja excepciones de todos los Controllers
public class GlobalExceptionHandler {

    // Maneja la excepción cuando el stock es insuficiente
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<String> handleStockInsuficienteException(StockInsuficienteException ex) {
        // 409 Conflict: Indica que la solicitud no pudo ser completada debido a un conflicto con el estado actual del recurso.
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // Código 409
    }

    // Maneja la excepción cuando el ID de un producto no existe
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<String> handleProductoNoEncontradoException(ProductoNoEncontradoException ex) {
        // 404 Not Found: El recurso solicitado no existe.
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // Código 404
    }
}