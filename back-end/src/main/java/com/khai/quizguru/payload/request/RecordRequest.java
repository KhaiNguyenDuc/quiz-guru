package com.khai.quizguru.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RecordRequest {

    @NotBlank(message = "Quiz's ID can't be empty")
    private String quizId;

    @NotBlank(message = "TimeLeft can't be empty")
    private Integer timeLeft;
    private List<RecordItemRequest> recordItems;
}
