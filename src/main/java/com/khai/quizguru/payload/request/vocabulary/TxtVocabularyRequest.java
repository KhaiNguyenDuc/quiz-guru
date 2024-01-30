package com.khai.quizguru.payload.request.vocabulary;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.utils.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class TxtVocabularyRequest extends VocabularyPromptRequest{
    private MultipartFile file;

    @Override
    public String getText() {
        if (Objects.isNull(this.file)) {
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
}
