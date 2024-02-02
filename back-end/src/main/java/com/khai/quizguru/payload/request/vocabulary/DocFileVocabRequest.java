package com.khai.quizguru.payload.request.vocabulary;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.utils.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
@EqualsAndHashCode(callSuper = true)
@Data
public class DocFileVocabRequest extends VocabularyPromptRequest {
    private MultipartFile file;

    @Override
    public String getText() {
        if (Objects.isNull(this.file)) {
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                if (originalFilename.toLowerCase().endsWith(".docx")) {
                    try (XWPFDocument document = new XWPFDocument(file.getInputStream());
                         XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
                        return extractor.getText();
                    }
                } else {
                    throw new InvalidRequestException(Constant.FILE_NOT_SUPPORT_MSG);
                }
            } else {
                throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
            }
        } catch (IOException e) {
            // Log the exception details for debugging purposes
            // e.g., logger.error("Error processing file: ", e);
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
    }
}
