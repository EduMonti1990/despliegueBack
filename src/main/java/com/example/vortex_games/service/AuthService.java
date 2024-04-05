package com.example.vortex_games.service;


import com.example.vortex_games.auth.AuthResponse;
import com.example.vortex_games.auth.EmailSenderService;
import com.example.vortex_games.auth.LoginRequest;
import com.example.vortex_games.auth.RegisterRequest;
import com.example.vortex_games.entity.Role;
import com.example.vortex_games.entity.User;
import com.example.vortex_games.repository.UserRepository;
import com.example.vortex_games.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailSenderService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode( request.getPassword()))// **Asi va si no lo queremos encriptar**--->.password(request.getPassword())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .direccion(request.getDireccion())
                .role(Role.USER)
                .build();
        userRepository.save(user);

        String emailSubject = user.getNombre() + " " + user.getApellido() + " su usuario VortexGames fué creado correctamente.";
        String emailBody = "Su usuario ha sido creado correctamente, puede entrar a su cuenta con el link www.votext.com";
        /* String htmlBody =
                "<html>"
                + "<body>"
                + "<h1 style=\"color:blue;\">¡Gracias por registrarte "
                + user.getNombre() + " " + user.getApellido() + "!</h1>"
                + "<h2>¡Tu cuenta ha sido creada exitosamente!</h2>"
                + "<h3>Accede a ella desde este link</h3>"
                + "<href="+"www.vortex.com"+">www.vortex.com</a>"
                + "</body>"
                + "</html>";
*/
        emailSenderService.sendSimpleEmail(
                request.getUsername(),
                emailSubject,
                request.getNombre(),
                request.getApellido());

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    public Optional<User> changeRole(User userRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(userRequest.getUsername());

        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setRole(userRequest.getRole());
            userRepository.save(user);
            return Optional.of(user);

        } else {
            // Manejar el caso en que el usuario no existe
            return Optional.empty();
        }
    }

    public List<User> listUsers(){return userRepository.findAll();}
}