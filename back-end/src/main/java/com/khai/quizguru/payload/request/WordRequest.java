package com.khai.quizguru.payload.request;

import lombok.Data;

@Data
public class WordRequest {

    private String name;
    private String definition;
    private String content;
}
