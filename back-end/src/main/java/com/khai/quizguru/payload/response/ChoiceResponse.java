package com.khai.quizguru.payload.response;

import lombok.Data;

@Data
public class ChoiceResponse {

    private String id;
    private String name;
    private Boolean isCorrect;
}
