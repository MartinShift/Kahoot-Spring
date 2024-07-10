package com.example.kahoot_front.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    private int id;

    private String title;

    private String content;

    private List<String> fileUrls;

    private User user;

}
