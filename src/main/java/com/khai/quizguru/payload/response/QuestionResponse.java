package com.khai.quizguru.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.enums.QuestionType;
import com.khai.quizguru.model.Choice;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class QuestionResponse {

    private String id;
    private String query;
    private List<ChoiceResponse> choices;
    private String explanation;
    private String type;

}
