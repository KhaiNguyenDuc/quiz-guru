package com.khai.quizguru.payload.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RecordItemRequest {

    @NotBlank(message = "Question's ID can't be empty")
    private String questionId;
    private List<String> selectedChoiceIds;
}
