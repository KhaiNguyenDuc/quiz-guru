package com.khai.quizguru.repository;

import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    List<Quiz> findAllByUser(User user);
}
