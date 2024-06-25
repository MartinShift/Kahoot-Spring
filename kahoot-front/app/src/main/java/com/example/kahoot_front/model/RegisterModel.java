package com.example.kahoot_front.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel {
    private String login;
    private String email;
    private String password;
    private String confirmPassword;
    private String avatar;
}