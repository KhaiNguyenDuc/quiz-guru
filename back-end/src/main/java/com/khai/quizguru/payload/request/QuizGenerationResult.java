package com.khai.quizguru.payload.request;

import com.khai.quizguru.dto.ChatResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data

public class QuizGenerationResult {
    private ChatResponse chatResponse;
    private String quizId;
    private String wordSetId;
}
