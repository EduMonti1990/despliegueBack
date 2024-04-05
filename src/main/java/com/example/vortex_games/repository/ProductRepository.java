package com.example.vortex_games.repository;


import com.example.vortex_games.entity.Calificacion;
import com.example.vortex_games.entity.Category;
import com.example.vortex_games.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findByName(String name);

    List<Product> findByCategory( Category category);


}