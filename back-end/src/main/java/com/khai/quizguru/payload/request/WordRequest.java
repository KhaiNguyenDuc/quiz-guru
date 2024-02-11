package com.khai.quizguru.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WordRequest {

    @NotBlank(message = "Name can't be empty")
    private String name;
    private String definition;
    private String content;
}
