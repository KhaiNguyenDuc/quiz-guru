package com.khai.quizguru.payload.request.Prompt;

import com.khai.quizguru.dto.Message;
import com.khai.quizguru.utils.Prompt;
import lombok.Data;

import java.util.List;

@Data
public abstract class PromptRequest {

    protected Integer type;
    protected Integer number;
    protected String language;
    protected Integer level;

    public abstract String getText();
    protected String getQuestionType(){

        return "Multiple choice";
    }

    protected String getLevel(){
        return switch (this.level) {
            case 2 -> "medium";
            case 3 -> "hard";
            default -> "easy";
        };
    }
    public String generatePrompt(){

        return String.format(Prompt.GENERATE_QUIZ_PROMPT,
                this.getNumber(),
                this.getQuestionType(),
                this.getLevel(),
                this.getLanguage());
    }

}
