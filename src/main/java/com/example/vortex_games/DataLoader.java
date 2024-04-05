package com.example.vortex_games;

import com.example.vortex_games.entity.Role;
import com.example.vortex_games.entity.User;
import com.example.vortex_games.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.stereotype.Component;

import org.springframework.security.crypto.password.PasswordEncoder;


@Component
public class DataLoader implements ApplicationRunner {
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Constructor
    @Autowired
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            String password = passwordEncoder.encode("admin");
            User admin = User.builder()
                    .username("admin")
                    .password(password)
                    .nombre("admin")
                    .apellido("admin")
                    .direccion("adminDir")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            String password2 = passwordEncoder.encode("user");
            User user = User.builder()
                    .username("user")
                    .password(password2)
                    .nombre("user")
                    .apellido("user")
                    .direccion("userDir")
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }
    }
}