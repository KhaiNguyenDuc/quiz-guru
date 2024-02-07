package com.khai.quizguru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class representing a message in a chat.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String role;
    private String content; // Content of the message, e.g., prompt

}
