package com.khai.quizguru.payload.request;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.List;

@Data
public class RecordItemRequest {
    private String questionId;
    private List<String> selectedChoiceIds;
}
