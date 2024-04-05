package com.example.vortex_games.controller;

import com.example.vortex_games.Dto.DtoBooking;
import com.example.vortex_games.Dto.DtoFechasBusqueda;
import com.example.vortex_games.entity.Booking;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.entity.User;
import com.example.vortex_games.exception.BadRequestException;
import com.example.vortex_games.exception.ResourceNotFoundException;
import com.example.vortex_games.service.BookingService;
import com.example.vortex_games.service.ProductService;
import com.example.vortex_games.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @PostMapping("/add-booking")
    public ResponseEntity<DtoBooking> agregarBooking(@RequestBody Booking booking) throws ResourceNotFoundException, BadRequestException {
        Optional<User> usuarioEncontrado= userService.buscarUsuarioPorName(booking.getUsuario().getUsername());
        if(!usuarioEncontrado.isPresent()){
            throw new ResourceNotFoundException("Usuario no existe");
        }
        else if (booking.getFechaInicio().isBefore(LocalDate.now())) {
            throw new BadRequestException("La fecha de inicio no puede ser menor a hoy");
        }
        else if (booking.getFechaFin().isBefore(booking.getFechaInicio())) {
            throw new BadRequestException("La fecha de finalizacion no puede ser menor a la fecha de inicio");
        }
        DtoBooking reserva=bookingService.addBooking(booking);
        if(reserva==null){
            throw new BadRequestException("Productos no disponibles");
        }
        return ResponseEntity.ok(reserva) ;
    }

    @GetMapping("/list-bookings")
    public ResponseEntity<List<DtoBooking>> listarBooking() throws ResourceNotFoundException {
        List<DtoBooking> reservas= bookingService.listaReservas();
        if(reservas.size()<=0){
            throw new ResourceNotFoundException("No existen reservas");
        }
        else{
            return ResponseEntity.ok(reservas);
        }
    }

   /* @PostMapping("/list-productos-disponibles")
    public ResponseEntity<List<Product>>  productosDisponibles(@RequestBody DtoFechasBusqueda dtoFechasBusqueda) throws ResourceNotFoundException, BadRequestException {
        List<Product> productDisponible= bookingService.ProductosDisponibles(dtoFechasBusqueda);

        LocalDate inicio = dtoFechasBusqueda.getInicio();
        LocalDate fin = dtoFechasBusqueda.getFin();

        if (inicio != null && inicio.isBefore(LocalDate.now())) {
            throw new BadRequestException("La fecha de inicio no puede ser menor a hoy");
        } else if (inicio != null && fin != null && fin.isBefore(inicio)) {
            throw new BadRequestException("La fecha de finalización no puede ser menor a la fecha de inicio");
        }

        if(productDisponible.size()<=0){
            throw new ResourceNotFoundException("No hay productos disponibles");
        }
        else {
            return ResponseEntity.ok(productDisponible);
        }
    }*/

    @PostMapping("/list-productos-disponibles")
    public ResponseEntity<List<Product>>  productosDisponibles(@RequestBody DtoFechasBusqueda dtoFechasBusqueda) throws ResourceNotFoundException, BadRequestException {
        List<Product> productDisponible= bookingService.ProductosDisponibles(dtoFechasBusqueda);

        LocalDate inicio = dtoFechasBusqueda.getInicio();
        LocalDate fin = dtoFechasBusqueda.getFin();

        if (inicio != null && inicio.isBefore(LocalDate.now())) {
            throw new BadRequestException("La fecha de inicio no puede ser menor a hoy");
        } else if (inicio != null && fin != null && fin.isBefore(inicio)) {
            throw new BadRequestException("La fecha de finalización no puede ser menor a la fecha de inicio");
        }

        if(productDisponible.size()<=0){
            throw new ResourceNotFoundException("No hay productos disponibles");
        }
        else {
            return ResponseEntity.ok(productDisponible);
        }
    }

    @GetMapping("/disponibilidadXProducto/{productId}")
    public ResponseEntity<List<DtoFechasBusqueda>> desponibilidadXProducto(@PathVariable Long productId) throws ResourceNotFoundException {
        Optional<Product> productoBuscado = productService.searchById(productId);
        if(productoBuscado.isEmpty()) throw new ResourceNotFoundException("No se encontro el producto");
        return ResponseEntity.ok(bookingService.fechasNoDisponiblesXProducto(productId));
    }


}
