package com.khai.quizguru.repository;

import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.WordSet;
import com.khai.quizguru.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {

    @Query("SELECT q FROM Quiz q WHERE q.isDeleted = false AND q.user = :user")
    Page<Quiz> findAllByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT COALESCE(COUNT(r), 0) FROM Record r WHERE r.quiz.wordSet = :wordSet")
    Integer getTotalReviewTimeForWordSet(@Param("wordSet") WordSet wordSet);
}
