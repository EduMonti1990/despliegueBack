package com.example.vortex_games.controller;

import com.example.vortex_games.Dto.DtoRequestFavoritos;
import com.example.vortex_games.entity.Favorite;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.entity.User;
import com.example.vortex_games.exception.BadRequestException;
import com.example.vortex_games.exception.ExistingProductException;
import com.example.vortex_games.exception.ResourceNotFoundException;
import com.example.vortex_games.service.FavoriteService;
import com.example.vortex_games.service.ProductService;
import com.example.vortex_games.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add-favorite")
    public ResponseEntity<Product> agregarFavorito(@RequestBody DtoRequestFavoritos dtoRequestFavoritos) throws ResourceNotFoundException, ExistingProductException {
        Optional<User> usuarioEncontrado= userService.buscarUsuarioPorName(dtoRequestFavoritos.getUsername());
        Optional<Product> productoEncontrado= productService.searchById(dtoRequestFavoritos.getId());

        if(!usuarioEncontrado.isPresent()){
            throw new ResourceNotFoundException("Usuario no existe");
        }
        else if (!productoEncontrado.isPresent()) {
            throw new ResourceNotFoundException("Producto no existe");
        }
        else if (productoEncontrado.isPresent()&& usuarioEncontrado.isPresent() ) {
            User usuarioActualizado=usuarioEncontrado.get();
            Product productoActualizado=productoEncontrado.get();
            List<Favorite> favoritos=new ArrayList<>();
            favoritos=favoriteService.listaFavoritos();
            for (Favorite favo: favoritos) {
                if(favo.getId_producto().equals(dtoRequestFavoritos.getId()) && favo.getId_Usuario().equals(usuarioActualizado.getId())){
                    throw new ExistingProductException("El usuario " + usuarioActualizado.getUsername() + " ya tiene registrado en favoritos este producto");
                }
            }

            favoriteService.addFavorito(productoActualizado,usuarioActualizado);
            return ResponseEntity.ok(productoActualizado);
        }
        return ResponseEntity.badRequest().build();
    }


    @DeleteMapping("/delete-favorite")
    public ResponseEntity<String> deleteFavorite(@RequestBody DtoRequestFavoritos dtoRequestFavoritos) throws ResourceNotFoundException {
        Optional<User> usuarioEncontrado= userService.buscarUsuarioPorName(dtoRequestFavoritos.getUsername());
        Optional<Product> productoEncontrado= productService.searchById(dtoRequestFavoritos.getId());

        if(!usuarioEncontrado.isPresent()){
            throw new ResourceNotFoundException("Usuario no existe");
        }
        else if (!productoEncontrado.isPresent()) {
            throw new ResourceNotFoundException("Producto no existe");
        }
        else if (productoEncontrado.isPresent()&& usuarioEncontrado.isPresent() ) {
            User usuarioActualizado=usuarioEncontrado.get();
            Product productoActualizado=productoEncontrado.get();
            List<Favorite> favoritos=new ArrayList<>();
            favoritos=favoriteService.listaFavoritos();
            for (Favorite favo: favoritos) {
                if(favo.getId_producto().equals(dtoRequestFavoritos.getId()) && favo.getId_Usuario().equals(usuarioActualizado.getId())){
                    favoriteService.DeleteFavorito(productoActualizado,usuarioActualizado);
                     return ResponseEntity.ok("El producto ha sido eliminado de los favoritos del usuario " + usuarioActualizado.getUsername());
                }

            }
            return ResponseEntity.ok("El producto no puedo ser eliminado");

        }
       return null;
    }


    @GetMapping("/listar-favoritos-usuario/{username}")
    public ResponseEntity<List<Product>> favoritosXUsuario(@PathVariable String username) throws ResourceNotFoundException {
        Optional<User> usuarioEncontrado=userService.buscarUsuarioPorName(username);
        User usuarioActualizado=usuarioEncontrado.get();
        if(!usuarioEncontrado.isPresent()){
            throw new ResourceNotFoundException("El usuario no existe");
        }
        else {
            return ResponseEntity.ok(favoriteService.ListarFavoritosXUsuario(usuarioActualizado));
        }
    }









}
