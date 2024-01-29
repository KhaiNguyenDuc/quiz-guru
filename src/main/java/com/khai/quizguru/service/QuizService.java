package com.khai.quizguru.service;
import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.payload.request.QuizGenerationResult;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.QuizResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface QuizService {
    QuizResponse findById(String id);

    QuizGenerationResult generateQuiz(ChatRequest chat, String userId);

   JsonPageResponse<QuizResponse> findAllByUserId(String id, Pageable pageable);

    void deleteById(String quizId, String userId);

    QuizGenerationResult generateQuizAndSaveWordSet(ChatRequest chat, String userId);
}
