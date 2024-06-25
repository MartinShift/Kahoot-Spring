package com.example.kahoot_front.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    private int id;

    private String name;

    private Category category;

    private List<Question> questions;

    private List<QuizResult> quizResults;
}