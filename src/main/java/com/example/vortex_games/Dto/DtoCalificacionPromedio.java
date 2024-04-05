package com.example.vortex_games.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoCalificacionPromedio {

    private String productName;
    private double calificacionPromedio;
    private long totalDeCalificaciones;
}
