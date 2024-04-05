package com.example.vortex_games.service;

import com.example.vortex_games.entity.Category;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.repository.CategoryRepository;
import com.example.vortex_games.repository.ImageRepository;
import com.example.vortex_games.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ProductRepository productRepository;

    //Manual Methods

    public Category addCategory(Category category){
        return categoryRepository.save(category);
    }

    public Optional<Category> searchByName(String title){
        return categoryRepository.findByTitle(title);
    }

    public void deleteCategory(Category category){
        Category categoriaVacia=new Category("Sin categoria","Producto sin categoria");
        for (Product product : category.getProducts()) {
            Optional<Category> categoriaEncontrada=categoryRepository.findByTitle(categoriaVacia.getTitle());
            if(categoriaEncontrada.isEmpty()){
                categoriaEncontrada=Optional.of(categoryRepository.save(categoriaVacia));
            }
            product.setCategory(categoriaEncontrada.get());

        }
        imageRepository.delete(category.getImage());
        categoryRepository.delete(category);
    }

    public Optional<Category> searchById(Long id){
        return categoryRepository.findById(id);
    }

    public void UpdateCategory(Category category){
        categoryRepository.save(category);
    }

    public List<Category> listCategory(){
        return categoryRepository.findAll();
    }

}


