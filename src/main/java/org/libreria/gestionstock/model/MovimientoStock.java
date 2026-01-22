package org.libreria.gestionstock.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class MovimientoStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    private String productoNombre;
    private Integer cantidadAnterior;
    private Integer cantidadNueva;
    private Integer diferencia; // Ejemplo: -2 para venta, +10 para compra
    private String tipoOperacion; // "VENTA", "AJUSTE_MANUAL", "COMPRA"
    private LocalDateTime fecha;

    public MovimientoStock() {
        this.fecha = LocalDateTime.now();
    }
}