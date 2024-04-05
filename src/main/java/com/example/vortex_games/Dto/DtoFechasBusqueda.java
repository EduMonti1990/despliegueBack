package com.example.vortex_games.Dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoFechasBusqueda {

    private LocalDate inicio;

    private LocalDate fin;


}
