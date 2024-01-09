package com.khai.quizguru.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String role;
    private String content;//prompt
}