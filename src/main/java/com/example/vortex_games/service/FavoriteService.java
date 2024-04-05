package com.example.vortex_games.service;

import com.example.vortex_games.entity.Favorite;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.entity.User;
import com.example.vortex_games.repository.FavoriteRepository;
import com.example.vortex_games.repository.ProductRepository;
import com.example.vortex_games.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    //Methods Manual

    public void addFavorito(Product producto, User user){
        Optional<Product> productoEncontrado=productRepository.findById(producto.getId());
        Optional<User> usuarioEncontrado=userRepository.findByUsername(user.getUsername());

        if(productoEncontrado.isPresent() && usuarioEncontrado.isPresent()){

            Product productoActualizado=productoEncontrado.get();
            User usuarioActualizado=usuarioEncontrado.get();
            Favorite favorito=new Favorite();
            favorito.setId_Usuario(usuarioActualizado.getId());
            favorito.setId_producto(productoActualizado.getId());
            favoriteRepository.save(favorito);
        }
    }

    public List<Favorite> listaFavoritos(){
        return favoriteRepository.findAll();
    }


    public void DeleteFavorito(Product product, User user){
        Optional<Product> productoEncontrado=productRepository.findById(product.getId());
        Optional<User> usuarioEncontrado=userRepository.findByUsername(user.getUsername());

        if(productoEncontrado.isPresent() && usuarioEncontrado.isPresent() ){
            Product productoActualizado=productoEncontrado.get();
            User usuarioActualizado=usuarioEncontrado.get();
            List<Favorite> favoritos=new ArrayList<>();
            favoritos=favoriteRepository.findAll();
            for (Favorite favo: favoritos) {
                if(favo.getId_producto().equals(productoActualizado.getId()) && favo.getId_Usuario().equals(usuarioActualizado.getId())){
                    favoriteRepository.deleteById(favo.getId());
                }
            }
        }
    }


    public List<Product> ListarFavoritosXUsuario(User user){
        Optional<User> usuarioEncontrado=userRepository.findById(user.getId());
        User usuarioActualizado=usuarioEncontrado.get();
        List<Favorite> favoritos=new ArrayList<>();
        favoritos=favoriteRepository.findAll();
        List<Product> todosLosProductos=new ArrayList<>();
        todosLosProductos=productRepository.findAll();
        List<Product> productosFavoritosXUsuario=new ArrayList<>();
        if(usuarioEncontrado.isPresent()){
            for (Favorite favo: favoritos) {
                if(favo.getId_Usuario().equals(usuarioActualizado.getId())){
                    for (Product prod: todosLosProductos ) {
                        if(prod.getId().equals(favo.getId_producto())){
                            productosFavoritosXUsuario.add(prod);
                        }
                    }
                }

            }

        }
        return productosFavoritosXUsuario;
    }











}
