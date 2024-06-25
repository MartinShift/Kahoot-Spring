package com.example.kahoot_front.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult {
    private int id;

    private User user;

    private Quiz quiz;

    private int score;

    private Duration timeSpent;
}