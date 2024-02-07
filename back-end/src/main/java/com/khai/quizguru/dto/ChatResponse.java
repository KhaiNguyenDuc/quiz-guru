package com.khai.quizguru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO class representing a chat response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private List<Choice> choices;

    /**
     * Inner class representing a choice in the chat response.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {

        private int index;
        private Message message;

    }

}
