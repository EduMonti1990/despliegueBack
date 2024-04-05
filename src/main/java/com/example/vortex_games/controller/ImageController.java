package com.example.vortex_games.controller;


import com.example.vortex_games.entity.Image;
import com.example.vortex_games.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/add-Image")
    public void add(@RequestBody Image image){
        imageService.addImage(image);
    }

    @GetMapping("/list-images")
    public ResponseEntity<List<Image>> getImages(){
        return ResponseEntity.ok(imageService.listImage());
    }
}