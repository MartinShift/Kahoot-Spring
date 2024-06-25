package com.example.kahoot.repository;

import com.example.kahoot.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizResultRepository extends JpaRepository<QuizResult, Integer> {
}