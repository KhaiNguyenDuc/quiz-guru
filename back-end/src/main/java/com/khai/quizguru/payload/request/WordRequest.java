package com.khai.quizguru.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WordRequest {

    private String name;
    private String definition;
    private String content;
}
