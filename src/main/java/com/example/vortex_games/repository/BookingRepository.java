package com.example.vortex_games.repository;

import com.example.vortex_games.entity.Booking;
import com.example.vortex_games.entity.Calificacion;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking , Long> {

    List<Booking> findByUsuarioAndProductosReservados(User usuario, Product producto);

}
