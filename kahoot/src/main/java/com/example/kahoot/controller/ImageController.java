package com.example.kahoot.controller;

import com.example.kahoot.model.LoginModel;
import com.example.kahoot.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String>  uploadImage(@RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok(imageService.save(file));
    }

    @GetMapping("/test")
    public LoginModel  test() {
        return new LoginModel("test", "test");
    }
}