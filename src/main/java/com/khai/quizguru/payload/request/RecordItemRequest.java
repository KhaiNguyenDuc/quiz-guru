package com.khai.quizguru.payload.request;

import com.khai.quizguru.model.Choice;
import com.khai.quizguru.model.Question;
import lombok.Data;

@Data
public class RecordItemRequest {
    private String questionId;
    private String selectedChoiceId;
}
