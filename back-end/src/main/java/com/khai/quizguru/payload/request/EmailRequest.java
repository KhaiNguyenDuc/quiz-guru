package com.khai.quizguru.payload.request;

import lombok.Data;

@Data
public class EmailRequest {

    private String subject;
    private String userId;
    private String to;
    private String from;
    private String body;
}
