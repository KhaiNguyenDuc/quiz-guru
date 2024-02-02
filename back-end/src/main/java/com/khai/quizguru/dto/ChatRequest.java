package com.khai.quizguru.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.payload.request.PromptRequest;
import com.khai.quizguru.utils.Prompt;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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


    public ChatRequest(String model, PromptRequest promptRequest) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.promptRequest = promptRequest;
        this.givenText = this.promptRequest.getText();
        this.generateMessages(givenText);
    }

    private void generateMessages(String givenText){
        this.messages.add(new Message("user", givenText));
        this.messages.add(new Message("user",this.promptRequest.generatePrompt()));

    }




    public void addMessages(Message message){
        this.messages.add(message);
    }
}