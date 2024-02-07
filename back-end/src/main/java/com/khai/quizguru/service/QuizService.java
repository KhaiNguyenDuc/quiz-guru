package com.khai.quizguru.service;

import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.payload.request.QuizGenerationResult;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.QuizResponse;
import org.springframework.data.domain.Pageable;


/**
 * Service interface of quiz.
 */
public interface QuizService {
    QuizResponse findById(String id);

    QuizGenerationResult generateQuiz(ChatRequest chat, String userId);

   JsonPageResponse<QuizResponse> findAllByUserId(String id, Pageable pageable);

    void deleteById(String quizId, String userId);

    QuizGenerationResult generateQuizAndSaveWordSet(ChatRequest chat, String userId);
}
