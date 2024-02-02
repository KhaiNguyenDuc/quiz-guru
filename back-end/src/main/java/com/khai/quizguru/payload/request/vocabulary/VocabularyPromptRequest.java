package com.khai.quizguru.payload.request.vocabulary;

import com.khai.quizguru.payload.request.PromptRequest;
import com.khai.quizguru.utils.Prompt;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class VocabularyPromptRequest extends PromptRequest {
    protected Integer numberOfWords;
    protected String wordSetId;
    protected String wordSetName = "";
    protected Boolean isDoQuiz = false;

    public String generatePrompt(){
        String prompt = "";
         if(this instanceof GenerateVocabularyRequest){
            prompt = Prompt.VOCABULARY_QUIZ_PROMPT;
            return String.format(prompt,
                    this.getNumber(),
                    this.getQuizType().getValue(),
                    this.getLevel().getValue(),
                    this.getLanguage());
        }else{
            prompt = Prompt.TEXT_TO_VOCAB_PROMPT;
            return String.format(prompt,
                    this.getNumber(),
                    this.getQuizType().getValue(),
                    this.getNumberOfWords(),
                    this.getLevel().getValue(),
                    this.getLanguage());
        }

    }
}

