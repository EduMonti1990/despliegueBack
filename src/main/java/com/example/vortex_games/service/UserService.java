package com.example.vortex_games.service;

import com.example.vortex_games.entity.User;
import com.example.vortex_games.repository.ProductRepository;
import com.example.vortex_games.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    //Methods Manual

    public Optional<User> buscarUsuarioPorName(String name){
        return userRepository.findByUsername(name);
    }

    public Optional<User> buscarPorId(Integer id){ return userRepository.findById(id);}


}
