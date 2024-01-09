package com.khai.quizguru.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.Question;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ChoiceResponse {

    private String id;
    private String name;
    private Boolean isCorrect;
}
