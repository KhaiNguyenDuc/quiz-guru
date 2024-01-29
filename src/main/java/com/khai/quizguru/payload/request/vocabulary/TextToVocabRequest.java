package com.khai.quizguru.payload.request.vocabulary;

import com.khai.quizguru.payload.request.PromptRequest;
import com.khai.quizguru.payload.request.text.BaseTextRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TextToVocabRequest extends VocabularyPromptRequest  {

    private String content;
    @Override
    public String getText() {
        return this.content;
    }
}
