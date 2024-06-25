package com.example.kahoot.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterModel {

    @NotBlank(message = "Login is mandatory")
    private String login;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "Password should be at least 8 characters, contain 1 uppercase, 1 lowercase letter and 1 number")
    private String password;

    @NotBlank(message = "Confirm Password is mandatory")
    private String confirmPassword;

    private String avatar;

}