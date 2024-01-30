package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.dto.ChatResponse;
import com.khai.quizguru.model.Question;
import com.khai.quizguru.repository.QuestionRepository;
import com.khai.quizguru.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {


    @Value(("${openai.api.url}"))
    private String apiURL;


    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate;

    @Override
    public List<Question> getAllQuestion() {
        return questionRepository.findAll();
    }

    @Override
    public ChatResponse quickChat(ChatRequest chat) {
        return restTemplate.postForObject(apiURL, chat, ChatResponse.class);
    }

}
