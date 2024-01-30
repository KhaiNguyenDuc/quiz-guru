package com.khai.quizguru.utils;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadUtils {
    public static Path saveUserImage(MultipartFile file, String userId) throws java.io.IOException {
        Path fileNameAndPath = Paths.get(
                System.getProperty("user.dir") +
                        Constant.UPLOAD_DIRECTORY+
                        Constant.UPLOAD_USER_DIRECTORY,
                userId + ".png");
        Files.write(fileNameAndPath, file.getBytes());
        return fileNameAndPath;
    }
}
