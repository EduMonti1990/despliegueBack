package com.example.vortex_games.repository;

import com.example.vortex_games.entity.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacteristicRepository extends JpaRepository<Characteristic,Long> {

    Optional<Characteristic> findByName(String name);
}
