package com.example.vortex_games.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int valor;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product producto;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    private String comentario;
}
