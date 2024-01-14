package com.khai.quizguru.payload.request.Prompt;

import com.khai.quizguru.dto.Message;
import com.khai.quizguru.utils.Prompt;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public abstract class PromptRequest {

    public Integer type;
    public Integer number;
    public String language;
    public Integer level;

    public abstract String getText();
    public String getQuestionType(){

        return "Multiple choice";
    }

    public String getLevel(){
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
