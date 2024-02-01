package com.khai.quizguru.controller;

import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
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

    @PostMapping("/word-set/{id}")
    public ResponseEntity<JsonResponse> getWordsDefinition(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String wordSetId,
            @RequestBody List<String> words
            ){

        List<WordResponse> wordResponses = wordService.findDefinition(wordSetId, words, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success",wordResponses), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> udpateWordDefinition(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name ="id") String wordId,
            @RequestBody WordRequest wordRequest
    ){

        WordResponse  wordResponse = wordService.udpateWordDefinition(wordId, wordRequest, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success",wordResponse), HttpStatus.OK);
    }
}
