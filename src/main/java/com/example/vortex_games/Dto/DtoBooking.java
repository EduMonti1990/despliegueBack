package com.example.vortex_games.Dto;

import com.example.vortex_games.entity.Booking;
import com.example.vortex_games.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoBooking {

    private Long id;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String userName;

    private List<DtopProductos> productos =new ArrayList<>();

}
