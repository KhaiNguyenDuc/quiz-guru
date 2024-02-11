package com.khai.quizguru.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RecordRequest {

    @NotBlank(message = "Quiz's ID can't be empty")
    private String quizId;

    @NotNull(message = "TimeLeft can't be empty")
    private Integer timeLeft;
    private List<RecordItemRequest> recordItems;
}
