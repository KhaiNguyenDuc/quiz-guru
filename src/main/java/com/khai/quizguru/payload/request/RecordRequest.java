package com.khai.quizguru.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class RecordRequest {
    private String quizId;
    private Integer timeLeft;
    private List<RecordItemRequest> recordItems;
}
