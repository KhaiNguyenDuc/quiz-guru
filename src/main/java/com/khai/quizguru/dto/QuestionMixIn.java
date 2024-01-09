package com.khai.quizguru.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.Choice;

import java.util.List;

public abstract class QuestionMixIn {
    @JsonIgnore
    private List<Choice> choices; // Ignoring this field during deserialization
}
