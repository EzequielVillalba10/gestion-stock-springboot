package org.libreria.gestionstock.exception;

// Hereda de RuntimeException para ser una excepción no chequeada
public class StockInsuficienteException extends RuntimeException {

    // Constructor que acepta un mensaje, lo pasaremos al ExceptionHandler
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}