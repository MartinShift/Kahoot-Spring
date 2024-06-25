package com.example.kahoot_front.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private int id;

    private String text;

    private Quiz quiz;

    private List<Option> options;

    private String imageUrl;
}