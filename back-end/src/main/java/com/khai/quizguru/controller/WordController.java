package com.khai.quizguru.controller;

import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller class for managing word-related operations.
 */
@RestController
@RequestMapping("/api/v1/words")
@RequiredArgsConstructor
public class WordController {


    private final WordService wordService;

    /**
     * Retrieves the definitions of the provided list of words belonging to a word set.
     * @param userPrincipal Current authenticated user
     * @param wordSetId ID of the word set containing the words
     * @param words List of words to retrieve definitions for
     * @return ResponseEntity with the list of word responses
     */
    @PostMapping("/word-set/{id}")
    public ResponseEntity<JsonResponse> getWordsDefinition(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String wordSetId,
            @RequestBody List<String> words
            ){

        List<WordResponse> wordResponses = wordService.findDefinition(wordSetId, words, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success",wordResponses), HttpStatus.OK);
    }

    /**
     * Updates the definition of a word.
     * @param userPrincipal Current authenticated user
     * @param wordId ID of the word to update
     * @param wordRequest Updated word information
     * @return ResponseEntity with the updated word response
     */
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> udpateWordDefinition(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name ="id") String wordId,
            @Valid @RequestBody WordRequest wordRequest
    ){

        WordResponse  wordResponse = wordService.udpateWordDefinition(wordId, wordRequest, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success",wordResponse), HttpStatus.OK);
    }
}
