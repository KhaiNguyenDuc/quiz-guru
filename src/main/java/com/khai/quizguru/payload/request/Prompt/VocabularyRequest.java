package com.khai.quizguru.payload.request.Prompt;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VocabularyRequest extends PromptRequest implements hasVocabulary {

    @Override
    public String getText() {
        return this.names.toString();
    }

    @Override
    public List<String> getNames() {
        return this.names;
    }
}
