package com.example.kahoot_front.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;

    private String username;

    private String password;

    private List<QuizResult> quizResults;

    private String avatarUrl;

    private String email;
}