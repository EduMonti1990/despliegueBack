package com.example.vortex_games.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Reservas")
@Entity
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @NonNull
    @Column
    private LocalDate fechaInicio;

    @NonNull
    @Column
    private LocalDate fechaFin;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="usuario_id")
    private User usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservas_productos_reservados",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private Set<Product> productosReservados = new HashSet<>();


}
