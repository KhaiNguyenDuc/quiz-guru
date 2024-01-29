package com.khai.quizguru.controller;

import com.khai.quizguru.exception.UnauthorizedException;
import com.khai.quizguru.payload.request.BindRequest;
import com.khai.quizguru.payload.request.WordSetRequest;
import com.khai.quizguru.payload.response.*;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.WordSetService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/word-set")
@RequiredArgsConstructor
public class WordSetController {

    private final WordSetService wordSetService;

    @PostMapping
    public ResponseEntity<JsonResponse> createWordSet(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody WordSetRequest wordSetRequest
            ){
        String id = wordSetService.createWordSet(wordSetRequest, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", id), HttpStatus.CREATED);
    }

    @PostMapping("/bind")
    public ResponseEntity<JsonResponse> bindQuiz(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody BindRequest bindRequest
            ){
        wordSetService.bindQuiz(bindRequest.getWordSetId(), bindRequest.getQuizId(), userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", "success"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonPageResponse<WordSetResponse>> findWordsByWordSetId(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String wordSetId,
            @RequestParam(name = "page", defaultValue ="0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size
    ){
        JsonPageResponse<WordSetResponse> words = wordSetService.findWordsById(wordSetId, userPrincipal.getId(), PageRequest.of(page, size));
        return new ResponseEntity<>(words, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<JsonResponse> deleteById(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String wordSetId
    ){
        wordSetService.deleteById(wordSetId, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", "success"), HttpStatus.OK);
    }
}
