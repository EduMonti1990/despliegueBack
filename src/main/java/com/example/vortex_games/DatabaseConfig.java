package com.example.vortex_games;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DatabaseConfig {

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

}