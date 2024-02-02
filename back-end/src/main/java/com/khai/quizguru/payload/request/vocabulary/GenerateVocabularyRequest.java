package com.khai.quizguru.payload.request.vocabulary;

import com.khai.quizguru.payload.request.PromptRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenerateVocabularyRequest extends VocabularyPromptRequest {
    List<String> names = new ArrayList<>();
    @Override
    public String getText() {
        return this.names.toString();
    }

}


