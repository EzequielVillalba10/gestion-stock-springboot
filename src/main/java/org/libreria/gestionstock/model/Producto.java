package org.libreria.gestionstock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="producto")
@Data //Genera los getters, setters, to String, equals y hashcode (lombook)
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    private String codigo;

    @Column(nullable=false)
    private String nombre;

    private String marca;

    @Column(name = "precio_venta")
    private Double precioVenta;


    @OneToOne(mappedBy = "producto" , cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("producto")
    private Stock stock;


}
