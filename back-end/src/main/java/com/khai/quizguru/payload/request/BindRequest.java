package com.khai.quizguru.payload.request;

import lombok.Data;

@Data
public class BindRequest {
    private String quizId;
    private String wordSetId;
}
