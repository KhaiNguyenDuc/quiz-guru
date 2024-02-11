package com.khai.quizguru.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileRequest {

    private String username;
    private MultipartFile file;
}
