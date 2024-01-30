package com.khai.quizguru.controller;

import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImageById(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String imageId
            ){

        Resource resource = imageService.findById(imageId);
        return new ResponseEntity<>(resource, HttpStatus.OK);

    }
}
