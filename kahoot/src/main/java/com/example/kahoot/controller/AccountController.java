package com.example.kahoot.controller;

import com.example.kahoot.entity.User;
import com.example.kahoot.model.LoginModel;
import com.example.kahoot.model.RegisterModel;
import com.example.kahoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterModel registerModel) {
        if (!registerModel.getPassword().equals(registerModel.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        if (userService.existsByUsername(registerModel.getLogin())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (userService.existsByEmail(registerModel.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        User savedUser = userService.saveUser(registerModel);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginModel loginModel) {
        User user = (User) userService.loadUserByUsername(loginModel.getLogin());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!userService.validatePassword(loginModel)) {
            return ResponseEntity.badRequest().body("Invalid password");
        }
        return ResponseEntity.ok(user);
    }
}