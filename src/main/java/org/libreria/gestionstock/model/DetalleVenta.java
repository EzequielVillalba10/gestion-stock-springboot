package org.libreria.gestionstock.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne: Muchos detalles pertenecen a una venta
    @ManyToOne(fetch = FetchType.LAZY) // LAZY para optimización
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonBackReference
    private Venta venta;

    // Relación ManyToOne: Guardamos el producto que se vendió
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnoreProperties({"stock"})
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad; // Cantidad vendida

    @Column(nullable = false)
    private Double precioUnitarioVenta; // El precio de venta en el momento de la venta

    // --- Constructor, Getters y Setters ---

    // Getters y Setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    public void setPrecioUnitarioVenta(Double precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }
}