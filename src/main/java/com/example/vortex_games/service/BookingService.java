package com.example.vortex_games.service;

import com.example.vortex_games.Dto.DtoBooking;
import com.example.vortex_games.Dto.DtoFechasBusqueda;
import com.example.vortex_games.Dto.DtopProductos;
import com.example.vortex_games.auth.EmailSenderService;
import com.example.vortex_games.entity.Booking;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.entity.User;
import com.example.vortex_games.exception.ResourceNotFoundException;
import com.example.vortex_games.repository.BookingRepository;
import com.example.vortex_games.repository.ProductRepository;
import com.example.vortex_games.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Log4j2
@Service

public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
     private ProductRepository productRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    public DtoBooking addBooking(Booking booking){
        Optional<User> usuarioReserva = userRepository.findByUsername(booking.getUsuario().getUsername());
        booking.setUsuario(usuarioReserva.get());
        Set<Product> productosEnReserva = new HashSet<>();
        DtoFechasBusqueda fechaReserva= new DtoFechasBusqueda(booking.getFechaInicio(),booking.getFechaFin());
        for (Product producto: booking.getProductosReservados()) {
            for (Product disponible: this.ProductosDisponibles(fechaReserva)) {
               if(producto.getName().equals(disponible.getName())){
                   productosEnReserva.add(disponible);
               }
            }
        }
        if(productosEnReserva.size()==booking.getProductosReservados().size()){
            booking.setProductosReservados(productosEnReserva);
           Booking bookingGuardado= bookingRepository.save(booking);
           this.mailConfirmacionReserva(booking);
            return bookingADto(bookingGuardado);
        }

        return null;

    }

    public List<DtoBooking> listaReservas(){
        List<DtoBooking> bookingsDto=new ArrayList<>();
        for (Booking book: bookingRepository.findAll()) {
            bookingsDto.add(bookingADto(book));
        }
        return  bookingsDto;
    }



    public List<Product> ProductosDisponibles(DtoFechasBusqueda dtoFechasBusqueda){
        List<Booking> reservas = bookingRepository.findAll();
        List<Product> productosDisponibles = new ArrayList<>();
        List<Product> productosDeLaAplicacion = productRepository.findAll();

        LocalDate inicio = dtoFechasBusqueda.getInicio();
        LocalDate fin = dtoFechasBusqueda.getFin();

        if (inicio == null && fin == null) return productosDeLaAplicacion;

        for (Product pro: productosDeLaAplicacion ) {
            boolean productoEnReserva = false;
            for (Booking reserva: reservas) {
                for (Product productoReservado: reserva.getProductosReservados()) {
                    boolean reservaEnRango = inicio == null || fin == null ||
                            (!inicio.isAfter(reserva.getFechaFin()) && !fin.isBefore(reserva.getFechaInicio()));
                    if (pro.getId().equals(productoReservado.getId()) && reservaEnRango) {
                        productoEnReserva = true;
                        break;
                    }
                }
                if(productoEnReserva) {
                    break;
                }
            }
            if (!productoEnReserva) {
                productosDisponibles.add(pro);
            }
        }
        return productosDisponibles;
    }

    public List<DtoFechasBusqueda> fechasNoDisponiblesXProducto(Long productId){
        Product productoBuscado = productRepository.findById(productId).get();
        List<Booking> reservas = bookingRepository.findAll();
        List<DtoFechasBusqueda> fechasReservadas = new ArrayList<>();
        for (Booking booking: reservas){
            DtoFechasBusqueda fechaInicioFin = new DtoFechasBusqueda();
            if((booking.getFechaFin().isAfter(LocalDate.now()) || booking.getFechaFin().isEqual(LocalDate.now())) && booking.getProductosReservados().contains(productoBuscado)){
                fechaInicioFin.setInicio(booking.getFechaInicio());
                fechaInicioFin.setFin(booking.getFechaFin());
                fechasReservadas.add(fechaInicioFin);
            }
        }
        return fechasReservadas;
    }

    public List<Booking> reservasFinalizadas(){
        List<Booking> reservasFinalizadas = new ArrayList<>();
        for (Booking booking: bookingRepository.findAll()){
            if(booking.getFechaFin().isBefore(LocalDate.now())) reservasFinalizadas.add(booking);
        }
        return reservasFinalizadas;
    }

    public void mailConfirmacionReserva(Booking bookin) {
        String emailSubject = bookin.getUsuario().getNombre() + " " + bookin.getUsuario().getApellido() + " Se registro una reserva con exito";
        StringBuilder emailBodyBuilder = new StringBuilder();
        emailBodyBuilder.append("<html><body style=\"font-family: Arial, sans-serif; background-color: #AC6ABA; padding: 20px;\">");

        // Agregar el banner al inicio del correo
        emailBodyBuilder.append("<div>");
        emailBodyBuilder.append("<img src=\"https://statics.vrutal.com/m/7412/7412eedb7df7fc7a1ede6a8d3a5974f2_thumb_fb.jpg\" alt=\"Banner\" style=\"width: 100%; height: auto; max-height: 200px;\">"); // Ajusta el tamaño máximo del banner
        emailBodyBuilder.append("</div>");

        emailBodyBuilder.append("<h2 style=\"color: #FFFFFF;\">¡Se registró una reserva con éxito!</h2>");
        emailBodyBuilder.append("<p style=\"color: #FFFFFF;\"><strong>Codigo de reserva:</strong>Vortex").append(bookin.getId()).append("</p>");
        emailBodyBuilder.append("<p style=\"color: #FFFFFF;\"><strong>Fecha y Hora:</strong>").append(LocalDate.now()).append("&nbsp;&nbsp;&nbsp;").append(formatTime(LocalTime.now())).append("</p>");
        emailBodyBuilder.append("<p style=\"color: #FFFFFF;\"><strong>Usuario:</strong> ").append(bookin.getUsuario().getNombre()).append(" ").append(bookin.getUsuario().getApellido()).append("</p>");
        emailBodyBuilder.append("<p style=\"color: #FFFFFF;\"><strong>Fecha de inicio de la reserva:</strong> ").append(bookin.getFechaInicio()).append("</p>");
        emailBodyBuilder.append("<p style=\"color: #FFFFFF;\"><strong>Fecha de finalización de la reserva:</strong> ").append(bookin.getFechaFin()).append("</p>");
        emailBodyBuilder.append("<p style=\"color: #FFFFFF;\"><strong>Juegos Reservados:</strong></p>");
        emailBodyBuilder.append("<ul style=\"color: #FFFFFF;\">");
        for (Product producto : bookin.getProductosReservados()) {
            emailBodyBuilder.append("<li>").append(producto.getName()).append("</li>");
        }
        emailBodyBuilder.append("</ul>");

        // Agregar el logo al final del correo
        emailBodyBuilder.append("<p style=\"color: #FFFFFF\"> <strong>Contacto: </strong> vortexgames19922024@gmail.com</p>");
        emailBodyBuilder.append("<div style=\"text-align: center; margin-top: 20px;\">");
        emailBodyBuilder.append("<img src=\"https://i.ibb.co/8z7rtYc/fondoblanco.png\" alt=\"Logo\" style=\"width: 100px; height: auto;\">");
        emailBodyBuilder.append("</div>");

        emailBodyBuilder.append("</body></html>");

        emailSenderService.sendEmailReserva(
                bookin.getUsuario().getUsername(),
                emailSubject,
                emailBodyBuilder.toString());
    }
    private String formatTime(LocalTime time){
        DateTimeFormatter horaFormateada = DateTimeFormatter.ofPattern("HH:mm:ss");
        return horaFormateada.format(time);
    }
    public DtoBooking bookingADto(Booking booking){
        DtoBooking dtoBooking=new DtoBooking();
        List<DtopProductos> productos=new ArrayList<>();
        dtoBooking.setId(booking.getId());
        dtoBooking.setFechaInicio(booking.getFechaInicio());
        dtoBooking.setFechaFin(booking.getFechaFin());
        dtoBooking.setUserName(booking.getUsuario().getUsername());
        for (Product pro: booking.getProductosReservados()) {
            productos.add(new DtopProductos(pro.getId(),pro.getName()));
        }
        dtoBooking.setProductos(productos);
        return dtoBooking;
    }




}
