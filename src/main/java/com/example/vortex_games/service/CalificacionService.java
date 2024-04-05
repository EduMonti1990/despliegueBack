package com.example.vortex_games.service;

import com.example.vortex_games.Dto.DtoCalificacion;
import com.example.vortex_games.Dto.DtoCalificacionPromedio;
import com.example.vortex_games.Dto.DtoCalificacionRequest;
import com.example.vortex_games.entity.Booking;
import com.example.vortex_games.entity.Calificacion;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.entity.User;
import com.example.vortex_games.repository.BookingRepository;
import com.example.vortex_games.repository.CalificacionRepository;
import com.example.vortex_games.repository.ProductRepository;
import com.example.vortex_games.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Log4j2
@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;

    public List<Calificacion> buscarPorUsuarioAndProducto(DtoCalificacionRequest dtoCalificacionRequest){
        User usuarioEncontrado = userRepository.findByUsername(dtoCalificacionRequest.getUsername()).get();
        Product productoEncontrado = productRepository.findById(dtoCalificacionRequest.getProductoId()).get();
        return calificacionRepository.findByUsuarioAndProducto(usuarioEncontrado,productoEncontrado);
    }

    public DtoCalificacion calificar(DtoCalificacionRequest dtoCalificacionRequest) {
        User usuarioEncontrado = userRepository.findByUsername(dtoCalificacionRequest.getUsername()).get();
        Product productoEncontrado = productRepository.findById(dtoCalificacionRequest.getProductoId()).get();
        Calificacion calificacion = new Calificacion();
        calificacion.setUsuario(usuarioEncontrado);
        calificacion.setProducto(productoEncontrado);
        calificacion.setValor(dtoCalificacionRequest.getValorCalificacion());
        if(dtoCalificacionRequest.getComentario()==null || dtoCalificacionRequest.getComentario().equals("")) calificacion.setComentario("Sin Comentarios");
        else calificacion.setComentario(dtoCalificacionRequest.getComentario());
        Calificacion calificacionGuardada = calificacionRepository.save(calificacion);

        //Con esta logica seteo el promedio de la calificacion en producto
        double valorSumado = productoEncontrado.getPromedioCalificaciones();
        double calPromedio = 0;
        List<Calificacion> calificacionesBuscadas = calificacionRepository.findByUsuarioAndProducto(usuarioEncontrado,productoEncontrado);
        for (Calificacion cal : calificacionesBuscadas) {
            valorSumado += cal.getValor();
        }
        log.info("Valor Sumado: "+valorSumado);
        log.info("Valor a dividir:"+productoEncontrado.getCalificacions().size());
        if (!calificacionesBuscadas.isEmpty()) {
            calPromedio = valorSumado / productoEncontrado.getCalificacions().size();
        }
        log.info("promedio: "+calPromedio);
        productoEncontrado.setPromedioCalificaciones(calPromedio);
        productRepository.save(productoEncontrado);

        return calificacionADto(calificacionGuardada) ;
    }

    public DtoCalificacionPromedio devolverPromedio(Long id){
        DtoCalificacionPromedio calificacionPromedio = new DtoCalificacionPromedio();
        Product productoBuscado = productRepository.findById(id).get();
        calificacionPromedio.setProductName(productoBuscado.getName());
        calificacionPromedio.setCalificacionPromedio(productoBuscado.getPromedioCalificaciones());
        calificacionPromedio.setTotalDeCalificaciones(productoBuscado.getCalificacions().size());
        return calificacionPromedio;

    }

    public Boolean finalizacionDeReserva(DtoCalificacionRequest dtoCalificacionRequest){
        User usuarioEncontrado = userRepository.findByUsername(dtoCalificacionRequest.getUsername()).get();
        Product productoEncontrado = productRepository.findById(dtoCalificacionRequest.getProductoId()).get();
        List<Booking> reservasDeUsuarioConProducto = bookingRepository.findByUsuarioAndProductosReservados(usuarioEncontrado,productoEncontrado);
        List<Booking> reservasFinalizadas = bookingService.reservasFinalizadas();
        for(Booking book: reservasFinalizadas){
            if(reservasDeUsuarioConProducto.contains(book)){
                return true;
            }
        }
        return false;
    }

    public List<DtoCalificacion> listarCalificacionesPorProducto(Long productId){
        Product productoBuscado = productRepository.findById(productId).get();
        List<Calificacion> calificacionesDelProducto = productoBuscado.getCalificacions();
        List<DtoCalificacion> calificaciones = new ArrayList<>() ;
        for (Calificacion calificacion : calificacionesDelProducto){
            calificaciones.add(calificacionADto(calificacion));
        }
        return calificaciones;
    }

    private DtoCalificacion calificacionADto(Calificacion calificacion){
        DtoCalificacion dtoCalificacion = new DtoCalificacion();
        dtoCalificacion.setId(calificacion.getId());
        dtoCalificacion.setUsername(calificacion.getUsuario().getUsername());
        dtoCalificacion.setProductoName(calificacion.getProducto().getName());
        dtoCalificacion.setProductId(calificacion.getProducto().getId());
        dtoCalificacion.setValorCalificacion(calificacion.getValor());
        dtoCalificacion.setComentario(calificacion.getComentario());
        return dtoCalificacion;
    }


}
