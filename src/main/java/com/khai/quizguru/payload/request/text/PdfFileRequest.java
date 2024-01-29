package com.khai.quizguru.payload.request.text;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.payload.request.PromptRequest;
import com.khai.quizguru.utils.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public class PdfFileRequest extends PromptRequest {

    private MultipartFile file;

    @Override
    public String getText() {
        if (this.file.isEmpty()) {
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".pdf"))) {
            throw new InvalidRequestException(Constant.FILE_NOT_SUPPORT_MSG);
        }
        try (PDDocument document = Loader.loadPDF(this.file.getBytes())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

    }

}
