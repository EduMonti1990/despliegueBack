package com.example.vortex_games.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Crea Getters & Setter de forma automática
@Builder //Construir los objetos de una manera limpia con el .builder
/*
Ejemplo de uso de este contructor:

Persona persona = Persona.builder()
                        .nombre("Juan")
                        .edad(30)
                        .ciudad("Ciudad de México")
                        .build();
Proporciona una forma más concisa y legible de construir objetos Persona en
comparación con un constructor tradicional o métodos setter.
* */
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequest {
    String username;
    String password;
}