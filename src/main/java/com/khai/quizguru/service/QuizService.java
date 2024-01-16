package com.khai.quizguru.service;
import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.payload.response.QuizResponse;

import java.util.List;


public interface QuizService {
    QuizResponse findById(String id);

    String generateQuiz(ChatRequest chat, String userId);

   List<QuizResponse> findAllByUserId(String id);
}
