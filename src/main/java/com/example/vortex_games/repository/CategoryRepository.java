package com.example.vortex_games.repository;

import com.example.vortex_games.entity.Category;
import com.example.vortex_games.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByTitle(String title);


}
