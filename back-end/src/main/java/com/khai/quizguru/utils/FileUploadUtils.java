package com.khai.quizguru.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for uploading files.
 */
public class FileUploadUtils {

    /**
     * Saves a user image file.
     *
     * @param file   The multipart file to be saved.
     * @param userId The ID of the user.
     * @return The path where the file is saved.
     * @throws IOException If an I/O error occurs while saving the file.
     */
    public static Path saveUserImage(MultipartFile file, String userId) throws IOException {
        Path fileNameAndPath = Paths.get(
                System.getProperty("user.dir") +
                        Constant.UPLOAD_DIRECTORY +
                        Constant.UPLOAD_USER_DIRECTORY,
                userId + ".png");
        Files.write(fileNameAndPath, file.getBytes());
        return fileNameAndPath;
    }
}
