package com.example.vortex_games.service;

import com.example.vortex_games.entity.Image;
import com.example.vortex_games.entity.Product;
import com.example.vortex_games.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    //Manual Methods

    public Image addImage(Image image){
        return imageRepository.save(image);
    }

    public List<Image> listImage(){
        return imageRepository.findAll();
    }

    public boolean hasDuplicateImages(Product product) {
        Set<String> uniqueImages = new HashSet<>();
        for (Image image : product.getImages()) {
            if (!uniqueImages.add(image.getImageUrl())) {
                return true;
            }
        }
        return false;
    }

}