package com.khai.quizguru.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BindRequest {

    @NotBlank(message = "Quiz id can't be empty")
    private String quizId;

    @NotBlank(message = "Word set id can't be empty")
    private String wordSetId;
}
