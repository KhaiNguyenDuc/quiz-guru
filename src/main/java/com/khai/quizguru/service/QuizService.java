package com.khai.quizguru.service;
import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.User.User;
import com.khai.quizguru.payload.response.QuizResponse;
import com.khai.quizguru.security.UserPrincipal;

import java.util.HashMap;
import java.util.List;


public interface QuizService {
    QuizResponse findById(String id);

    String generateQuiz(ChatRequest chat, String userId);

    HashMap<String, List<String>> findAllByUserId(String id);
}
