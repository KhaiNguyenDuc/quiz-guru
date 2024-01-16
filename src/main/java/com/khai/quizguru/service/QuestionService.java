package com.khai.quizguru.service;

import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.dto.ChatResponse;
import com.khai.quizguru.model.question.Question;

import java.util.List;

public interface QuestionService {


    List<Question> getAllQuestion();

    ChatResponse quickChat(ChatRequest chat);



}
