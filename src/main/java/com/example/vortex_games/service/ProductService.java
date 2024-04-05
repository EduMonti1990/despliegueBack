package com.example.vortex_games.service;


import com.example.vortex_games.entity.Category;
import com.example.vortex_games.entity.Characteristic;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.repository.CategoryRepository;
import com.example.vortex_games.repository.CharacteristicRepository;
import com.example.vortex_games.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CharacteristicRepository characteristicRepository;


    //Manual Methods

    public Product addProduct(Product producto){
        log.info(producto);
        if(producto.getCategory().getId()==null){
            Category categoriaVacia=new Category("Sin categoria","Producto sin categoria");
            Optional<Category> categoriaEncontrada=categoryRepository.findByTitle(categoriaVacia.getTitle());
            if(categoriaEncontrada.isEmpty()){
                categoriaVacia=categoryRepository.save(categoriaVacia);
                producto.setCategory(categoriaVacia);
            }
            else{
                producto.setCategory(categoriaEncontrada.get());
            }

        }

        producto.getImages().forEach(image -> image.setProduct(producto));
        return productRepository.save(producto);
    }


   /* public List<Product> searchByCategory(String category) {
        Category categoriaEncontrada=categoryRepository.findByTitle(category).get();
         return productRepository.findByCategory(categoriaEncontrada);
    }*/

    public List<Product> searchByCategory(String category) {
        Optional<Category> optionalCategory = categoryRepository.findByTitle(category);
        if (optionalCategory.isPresent()) {
            Category categoriaEncontrada = optionalCategory.get();
            return productRepository.findByCategory(categoriaEncontrada);
        } else {
            // Manejar el caso en que la categoría no se encuentra en la base de datos
            return Collections.emptyList();
        }
    }

    public List<Product> searchByCharacteristic(String characteristic){
        Characteristic characteristicEncontrada = characteristicRepository.findByName(characteristic).get();
        List<Product> productos = new ArrayList<>();
        for(Product product: productRepository.findAll()){
            for(Characteristic characteristic1: product.getCharacteristics()){
                if(characteristic1.equals(characteristicEncontrada)){
                    productos.add(product);
                }
            }
        }
        return productos;
    }



    public Optional<Product> searchById(Long id){
        return productRepository.findById(id);
    }
    public Optional<Product> searchByName(String name){
        return productRepository.findByName(name);
    }
    public List<Product> listProducts(){
        return productRepository.findAll();
    }


    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public void updateProduct(Product product){
        productRepository.save(product);
        /*productRepository.deleteById(product.getId());
        this.addProduct(product);*/
    }


   /* public void updateProduct(Product product) {
        // Obtener el producto existente por su ID
        Optional<Product> existingProductOptional = productRepository.findById(product.getId());

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();

            // Actualizar los atributos del producto existente con los valores proporcionados
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());


            // Guardar el producto actualizado
            productRepository.save(existingProduct);
        } else {
            // Manejar el caso en el que el producto no existe en la base de datos
            // Aquí puedes lanzar una excepción, registrar un mensaje de error, etc.
        }
    }*/


}