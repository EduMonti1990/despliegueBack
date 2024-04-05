package com.example.vortex_games.auth;


import com.example.vortex_games.entity.User;
import com.example.vortex_games.exception.ResourceNotFoundException;
import com.example.vortex_games.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PutMapping (value = "changeRol")
    public ResponseEntity<Optional<User>> changeRol(@RequestBody User user)  throws ResourceNotFoundException {
        String usernameAdmin = user.getUsername();
        if (!usernameAdmin.equals("ADMIN")) {
            return ResponseEntity.ok(authService.changeRole(user));
        } else {
            throw new ResourceNotFoundException("No se puede cambiar el rol del usuario Admin");
        }
    }

    @GetMapping(value = "listar-usuarios")
    public ResponseEntity<List<User>> listUsers() throws ResourceNotFoundException{
        List<User> listaUsuarios=authService.listUsers();
        if(!listaUsuarios.isEmpty()){
            return ResponseEntity.ok(listaUsuarios);
        }
        else {
            throw  new ResourceNotFoundException("No hay usuarios registrados");
        }
    }
}