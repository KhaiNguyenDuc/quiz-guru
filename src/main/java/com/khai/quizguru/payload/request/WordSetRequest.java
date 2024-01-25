package com.khai.quizguru.payload.request;

import com.khai.quizguru.model.Word;
import lombok.Data;

import java.util.List;

@Data
public class WordSetRequest {

    private String name;
    private String quizId;
    private List<WordRequest> words;
}
