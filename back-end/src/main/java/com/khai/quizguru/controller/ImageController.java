package com.khai.quizguru.controller;

import com.khai.quizguru.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing image-related operations.
 */
@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * Return image by its ID
     * @param imageId: ID of an image
     * @return Resource: base64 image
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImageById(
            @PathVariable("id") String imageId
            ){

        Resource resource = imageService.findById(imageId);
        return new ResponseEntity<>(resource, HttpStatus.OK);

    }
}
