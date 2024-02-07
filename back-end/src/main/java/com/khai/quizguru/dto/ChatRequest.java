package com.khai.quizguru.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.payload.request.PromptRequest;
import com.khai.quizguru.utils.Prompt;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO class representing a chat request.
 */
@Data
public class ChatRequest {

    private String model;
    private List<Message> messages;

    @JsonIgnore
    private PromptRequest promptRequest;

    @JsonIgnore
    private String basePrompt = Prompt.SINGLE_CHOICE_QUIZ_PROMPT;

    @JsonIgnore
    private String givenText = "";

    /**
     * Constructs a ChatRequest object.
     * @param model The model to be used for the chat.
     * @param promptRequest The prompt request object.
     */
    public ChatRequest(String model, PromptRequest promptRequest) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.promptRequest = promptRequest;
        this.givenText = this.promptRequest.getText();
        this.generateMessages(givenText);
    }

    /**
     * Generates messages based on the given text and prompt request.
     * @param givenText The given text.
     */
    private void generateMessages(String givenText){
        this.messages.add(new Message("user", givenText));
        this.messages.add(new Message("user",this.promptRequest.generatePrompt()));
    }

    /**
     * Adds a message to the list of messages.
     * @param message The message to add.
     */
    public void addMessages(Message message){
        this.messages.add(message);
    }
}
