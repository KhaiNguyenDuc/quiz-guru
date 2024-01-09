package com.khai.quizguru.payload.request.Prompt;

import com.khai.quizguru.Exception.InvalidRequestException;
import com.khai.quizguru.utils.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@EqualsAndHashCode(callSuper = true)
@Data
public class DocFileRequest extends PromptRequest {

    private MultipartFile file;

    @Override
    public String getText() {
        if (file.isEmpty()) {
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        try{
            String originalFilename = file.getOriginalFilename();
            if (originalFilename!= null && (originalFilename.toLowerCase().endsWith(".doc")
                    || originalFilename.toLowerCase().endsWith(".docx"))){
                Tika tika = new Tika();
                try (InputStream inputStream = file.getInputStream()) {
                    return tika.parseToString(inputStream);
                } catch (IOException | TikaException e) {
                    throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
                }
            }else{
                throw new InvalidRequestException(Constant.FILE_NOT_SUPPORT_MSG);
            }
        }catch (Exception e){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }




    }


}
