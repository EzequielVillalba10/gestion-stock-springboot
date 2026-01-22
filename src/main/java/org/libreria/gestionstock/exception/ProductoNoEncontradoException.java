package org.libreria.gestionstock.exception;

public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}