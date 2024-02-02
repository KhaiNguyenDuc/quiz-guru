package com.khai.quizguru.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class RecordItemResponse {
    private QuestionResponse question;
    private List<ChoiceResponse> selectedChoices;
}
