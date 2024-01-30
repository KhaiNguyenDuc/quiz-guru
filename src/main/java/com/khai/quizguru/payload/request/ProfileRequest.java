package com.khai.quizguru.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileRequest {

    private String username;
    private MultipartFile file;
}
