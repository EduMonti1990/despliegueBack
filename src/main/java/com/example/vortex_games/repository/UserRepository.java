package com.example.vortex_games.repository;

import com.example.vortex_games.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer> {
    Optional<User> findByUsername(String username);
}