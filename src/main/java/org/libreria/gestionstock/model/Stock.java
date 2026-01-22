package org.libreria.gestionstock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Integer cantidad;

    @Column(name="sotck_minimo", nullable=false)
    private Integer StockMinimo; //cantidad actual en stock

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="producto_id", referencedColumnName = "id", unique = true,nullable = false)
    @JsonIgnoreProperties("stock")
    private Producto producto;

}
