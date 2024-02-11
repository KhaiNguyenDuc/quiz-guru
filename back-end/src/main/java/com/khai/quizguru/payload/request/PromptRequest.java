package com.khai.quizguru.payload.request;

import com.khai.quizguru.enums.Level;
import com.khai.quizguru.enums.QuizType;
import com.khai.quizguru.payload.request.vocabulary.GenerateVocabularyRequest;
import com.khai.quizguru.payload.request.vocabulary.TextToVocabRequest;
import com.khai.quizguru.utils.Prompt;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class PromptRequest {

    @NotBlank(message = "Type can't be empty")
    public Integer type;

    @NotBlank(message = "Number can't be empty")
    public Integer number;

    @NotBlank(message = "Language can't be empty")
    public String language;

    @NotBlank(message = "Level can't be empty")
    public Integer level;

    @NotNull(message = "Duration can't be empty")
    public Integer duration;


    public abstract String getText();

    public QuizType getQuizType(){
        return switch (this.type){
            case 1 -> QuizType.SINGLE_CHOICE_QUESTION;
            case 2 -> QuizType.MULTIPLE_CHOICE_QUESTION;
            default -> QuizType.MIX_QUESTION;
        };
    }

    public Level getLevel(){
        return switch (this.level) {
            case 2 -> Level.MEDIUM;
            case 3 -> Level.HARD;
            default -> Level.EASY;
        };
    }
    public String generatePrompt(){
        String prompt = "";
        if(this instanceof GenerateVocabularyRequest){
            prompt = Prompt.VOCABULARY_QUIZ_PROMPT;
        }else if(this instanceof TextToVocabRequest){
           prompt = Prompt.TEXT_TO_VOCAB_PROMPT;
        } else {
            prompt = switch (this.type){
                case 2 -> Prompt.MULTIPLE_CHOICE_QUIZ_PROMPT;
                case 3 -> Prompt.MIX_CHOICE_QUIZ_PROMPT;
                default -> Prompt.SINGLE_CHOICE_QUIZ_PROMPT;
            };
        }

        return String.format(prompt,
                this.getNumber(),
                this.getQuizType().getValue(),
                this.getLevel().getValue(),
                this.getLanguage());
    }

}
