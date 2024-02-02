package com.khai.quizguru.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
public class JsonResponse {

    private String message;
    private Object data;
}
