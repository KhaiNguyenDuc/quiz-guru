package com.khai.quizguru.controller;

import com.khai.quizguru.payload.request.BindRequest;
import com.khai.quizguru.payload.request.WordSetRequest;
import com.khai.quizguru.payload.response.*;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.WordSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing word-set operations.
 */
@RestController
@RequestMapping("/api/v1/word-set")
@RequiredArgsConstructor
public class WordSetController {

    private final WordSetService wordSetService;

    /**
     * Creates a new word set.
     * @param userPrincipal Current authenticated user
     * @param wordSetRequest Request containing details of the word set to create
     * @return ResponseEntity with the ID of the created word set
     */
    @PostMapping
    public ResponseEntity<JsonResponse> createWordSet(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody WordSetRequest wordSetRequest
            ){
        String id = wordSetService.createWordSet(wordSetRequest, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", id), HttpStatus.CREATED);
    }

    /**
     * Updates an existing word set.
     * @param userPrincipal Current authenticated user
     * @param wordSetRequest Request containing updated details of the word set
     * @param wordSetId ID of the word set to update
     * @return ResponseEntity with the updated word set response
     */
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> updateWordSet(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody WordSetRequest wordSetRequest,
            @PathVariable("id") String wordSetId
    ){
        WordSetResponse wordSetResponse = wordSetService.updateWordSet(wordSetRequest, wordSetId, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", wordSetResponse), HttpStatus.OK);
    }


    /**
     * Binds a quiz to a word set.
     * @param userPrincipal Current authenticated user
     * @param bindRequest Request containing the IDs of the word set and quiz to bind
     * @return ResponseEntity indicating the success of the operation
     */
    @PostMapping("/bind")
    public ResponseEntity<JsonResponse> bindQuiz(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody BindRequest bindRequest
            ){
        wordSetService.bindQuiz(bindRequest.getWordSetId(), bindRequest.getQuizId(), userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", "success"), HttpStatus.CREATED);
    }

    /**
     * Retrieves words belonging to a word set by its ID.
     * @param userPrincipal Current authenticated user
     * @param wordSetId ID of the word set
     * @param page Page number (default is 0)
     * @param size Number of items per page (default is 10)
     * @return ResponseEntity with a page of word set responses
     */
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

    /**
     * Deletes a word set by its ID.
     * @param userPrincipal Current authenticated user
     * @param wordSetId ID of the word set to delete
     * @return ResponseEntity indicating the success of the operation
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<JsonResponse> deleteById(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String wordSetId
    ){
        wordSetService.deleteById(wordSetId, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", "success"), HttpStatus.OK);
    }
}
