package com.khai.quizguru.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonResponse {

    private String message;
    private Object data;
}
