package com.example.kahoot_front.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private int id;

    private QuizResult quizResult;

    private Option option;
}