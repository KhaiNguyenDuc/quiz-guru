package com.khai.quizguru.payload.request.Prompt;

import com.khai.quizguru.Exception.InvalidRequestException;
import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.utils.Constant;
import com.khai.quizguru.utils.Prompt;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public class TxtFileRequest extends PromptRequest {

    private MultipartFile file;

    @Override
    public String getText() {
        if (this.file.isEmpty()) {
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".txt"))) {
            throw new InvalidRequestException(Constant.FILE_NOT_SUPPORT_MSG);
        }
        try{
            return new String(file.getBytes());
        }catch (Exception e){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

    }

    @Override
    public String generatePrompt(){

        return String.format(Prompt.GENERATE_QUIZ_PROMPT,
                this.getNumber(),
                this.getQuestionType(),
                this.getLevel(),
                this.getLanguage());
    }
}
