package com.khai.quizguru.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.Choice;

import java.util.List;

/**
 * Mixin class to configure JSON serialization and deserialization for the Question class.
 * This class is used to ignore the 'choices' field during deserialization.
 */
public abstract class QuestionMixIn {
    @JsonIgnore
    private List<Choice> choices; // Ignoring this field during deserialization
}
