package com.example.kahoot.service;

import com.example.kahoot.entity.QuizResult;
import com.example.kahoot.repository.QuizResultRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizResultService {

    @Autowired
    private QuizResultRepository quizResultRepository;

    public QuizResult saveQuizResult(QuizResult quizResult) {
        return quizResultRepository.save(quizResult);
    }

    public List<QuizResult> getQuizResults() {
        return quizResultRepository.findAll();
    }

    public QuizResult getQuizResultById(int id) {
        return quizResultRepository.findById(id).orElse(null);
    }

    public void deleteQuizResult(int id) {
        quizResultRepository.deleteById(id);
    }
}