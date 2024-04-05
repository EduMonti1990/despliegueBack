package com.example.vortex_games.controller;

import com.example.vortex_games.Dto.DtoCalificacion;
import com.example.vortex_games.Dto.DtoCalificacionPromedio;
import com.example.vortex_games.Dto.DtoCalificacionRequest;
import com.example.vortex_games.entity.Calificacion;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.entity.User;
import com.example.vortex_games.exception.BadRequestException;
import com.example.vortex_games.exception.ResourceNotFoundException;
import com.example.vortex_games.service.CalificacionService;
import com.example.vortex_games.service.ProductService;
import com.example.vortex_games.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @PostMapping("/calificar")//<---- Metodo que sirve para que un usuario califique un producto
    public ResponseEntity<DtoCalificacion> calificar(@RequestBody DtoCalificacionRequest dtoCalificacionRequest) throws BadRequestException, ResourceNotFoundException {
        Optional<User> usuarioBuscado = userService.buscarUsuarioPorName(dtoCalificacionRequest.getUsername());
        Optional<Product> productoBuscado = productService.searchById(dtoCalificacionRequest.getProductoId());
        if(usuarioBuscado.isEmpty()) throw new ResourceNotFoundException("Usuario no registrado");
        if(productoBuscado.isEmpty()) throw new ResourceNotFoundException("Producto no encontrado");
        if(!calificacionService.finalizacionDeReserva(dtoCalificacionRequest)) throw new BadRequestException("El usuario debe haber reservado el juego previamente, y la reserva debe haber finalizado para poder puntuar");
        if(dtoCalificacionRequest.getValorCalificacion()<1 || dtoCalificacionRequest.getValorCalificacion()>5) throw new BadRequestException("El valor de la calificacion debe estar entre 1 y 5");
        List<Calificacion> calificacionesBuscadas = calificacionService.buscarPorUsuarioAndProducto(dtoCalificacionRequest);
        if(calificacionesBuscadas.size()>0) throw new BadRequestException("El usuario puede votar una sola ves por producto");
        return ResponseEntity.ok(calificacionService.calificar(dtoCalificacionRequest));
    }

    @GetMapping("/calificacionPromedio/{id}")//<--- Metodo que devuelve el promedio de las calificaciones totales que tiene un producto
    public ResponseEntity<DtoCalificacionPromedio> calificacionPromedio(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Product> productoBuscado = productService.searchById(id);
        if(productoBuscado.isEmpty()) throw new ResourceNotFoundException("No se encontro el producto");
        return ResponseEntity.ok(calificacionService.devolverPromedio(id));
    }

    @GetMapping("/calificacionDeUnProducto/{productId}")
    public ResponseEntity<List<DtoCalificacion>> calificacionDeUnProducto(@PathVariable Long productId) throws ResourceNotFoundException {
        Optional<Product> productoBuscado = productService.searchById(productId);
        if(productoBuscado.isEmpty()) throw new ResourceNotFoundException("El producto no existe");
        return ResponseEntity.ok(calificacionService.listarCalificacionesPorProducto(productId));
    }
}

