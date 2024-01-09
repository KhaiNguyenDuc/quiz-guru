package com.khai.quizguru.payload.request.Prompt;

import com.khai.quizguru.utils.Prompt;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseTextRequest extends PromptRequest {

    private String content;

    @Override
    public String getText() {
        return this.content;
    }



}
