package com.khai.quizguru.controller;

import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.service.WordService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/words")
@RequiredArgsConstructor
public class WordController {


    private final WordService wordService;

    @PostMapping
    public ResponseEntity<JsonResponse> getWordsDefinition(
            @RequestBody List<String> words
            ){

        List<Object> objects = wordService.findDefinition(words);
        return new ResponseEntity<>(new JsonResponse("success",objects), HttpStatus.OK);
    }
}
