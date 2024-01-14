package com.khai.quizguru.payload.response;

import lombok.Data;

@Data
public class RecordItemResponse {
    private QuestionResponse question;
    private ChoiceResponse selectedChoice;
}
