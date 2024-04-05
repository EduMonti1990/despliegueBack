package com.example.vortex_games.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoCalificacion {
    private long id;
    private String username;
    private String productoName;
    private Long productId;
    private Integer valorCalificacion;
    private String comentario;

}
