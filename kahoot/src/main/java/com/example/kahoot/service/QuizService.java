package com.example.kahoot.service;

import com.example.kahoot.entity.Quiz;
import com.example.kahoot.repository.QuizRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(int id) {
        return quizRepository.findById(id).orElse(null);
    }

    public void deleteQuiz(int id) {
        quizRepository.deleteById(id);
    }
}