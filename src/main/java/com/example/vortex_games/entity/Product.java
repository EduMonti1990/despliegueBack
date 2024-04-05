package com.example.vortex_games.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Table(name="Products")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String name;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @NonNull
    @Column(nullable = false)
    private String description;

    @NonNull
    @Column(nullable = false)
    private double price;

    @NonNull
    @Column(nullable = false)
    private String type;

    @NonNull
    @Column(nullable = false)
    private String console;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Image> images=new HashSet<>();


    @ManyToMany(cascade =  CascadeType.MERGE)
    @JoinTable(
            name = "producto_caracteristicas",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "characteristic_id")
    )
    private Set<Characteristic> characteristics=new HashSet<>();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<Calificacion> calificacions;

    @NonNull
    private double promedioCalificaciones=0;



}