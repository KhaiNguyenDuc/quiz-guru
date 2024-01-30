package com.khai.quizguru.payload.request.vocabulary;

import com.khai.quizguru.payload.request.HasHtmlContent;
import com.khai.quizguru.payload.request.PromptRequest;
import com.khai.quizguru.payload.request.text.BaseTextRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TextToVocabRequest extends VocabularyPromptRequest implements HasHtmlContent {

    private String content;
    private String htmlContent;
    @Override
    public String getText() {
        return this.content;
    }

    @Override
    public String getHtmlContent() {
        return this.htmlContent;
    }
}
