package com.example.kahoot_front.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    private int id;

    private String text;

    private Question question;

    private boolean isCorrect;
}