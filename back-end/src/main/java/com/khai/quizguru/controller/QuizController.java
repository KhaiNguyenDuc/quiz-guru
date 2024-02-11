package com.khai.quizguru.controller;

import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.exception.InternalErrorException;
import com.khai.quizguru.payload.request.QuizGenerationResult;
import com.khai.quizguru.payload.request.text.BaseTextRequest;
import com.khai.quizguru.payload.request.text.DocFileRequest;
import com.khai.quizguru.payload.request.text.PdfFileRequest;
import com.khai.quizguru.payload.request.text.TxtFileRequest;
import com.khai.quizguru.payload.request.vocabulary.*;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.QuizService;
import com.khai.quizguru.utils.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller class for managing user-related operations.
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @Value("${openai.model}")
    private String model;

    /**
     * Generates a quiz based on text content from a .txt file.
     * @param userPrincipal Current authenticated user
     * @param txtFileRequest Request containing information about the .txt file
     * @return ResponseEntity with the ID of the generated quiz
     */
    @PostMapping("/generate/txt")
    public ResponseEntity<JsonResponse> generateQuizByTxtFile(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @ModelAttribute TxtFileRequest txtFileRequest) {

        ChatRequest chat = new ChatRequest(model, txtFileRequest);
        QuizGenerationResult result = quizService.generateQuiz(chat, userPrincipal.getId());
        String quizId = result.getQuizId();
        return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);

    }

    /**
     * Generates a quiz based on text content from a .pdf file.
     * @param userPrincipal Current authenticated user
     * @param pdfFileRequest Request containing information about the .pdf file
     * @return ResponseEntity with the ID of the generated quiz
     */
    @PostMapping("/generate/pdf")
    public ResponseEntity<JsonResponse> generateQuizByPdfFile(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @ModelAttribute PdfFileRequest pdfFileRequest) {

        ChatRequest chat = new ChatRequest(model, pdfFileRequest);
        QuizGenerationResult result = quizService.generateQuiz(chat, userPrincipal.getId());
        String quizId = result.getQuizId();
        return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);

    }

    /**
     * Generates a quiz based on text content from a .doc file.
     * @param userPrincipal Current authenticated user
     * @param docFileRequest Request containing information about the .doc file
     * @return ResponseEntity with the ID of the generated quiz
     */
    @PostMapping("/generate/doc")
    public ResponseEntity<JsonResponse> generateQuizByDocFile(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @ModelAttribute DocFileRequest docFileRequest) {

        ChatRequest chat = new ChatRequest(model, docFileRequest);
        QuizGenerationResult result = quizService.generateQuiz(chat, userPrincipal.getId());
        String quizId = result.getQuizId();
        return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);

    }

    /**
     * Generates a quiz based on plain text.
     * @param userPrincipal Current authenticated user
     * @param baseTextRequest Request containing the plain text
     * @return ResponseEntity with the ID of the generated quiz
     */
    @PostMapping("/generate/text")
    public ResponseEntity<JsonResponse> generateQuizByText(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody BaseTextRequest baseTextRequest) {

            ChatRequest chat = new ChatRequest(model, baseTextRequest);
            QuizGenerationResult result = quizService.generateQuiz(chat, userPrincipal.getId());
            String quizId = result.getQuizId();
            return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);
    }

    /**
     * Generates a quiz based on vocabulary data.
     * @param userPrincipal Current authenticated user
     * @param generateVocabularyRequest Request containing the vocabulary data
     * @return ResponseEntity with the ID of the generated quiz
     */
    @PostMapping("/generate/vocabulary")
    public ResponseEntity<JsonResponse> generateQuizByVocabulary(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody GenerateVocabularyRequest generateVocabularyRequest) {

        ChatRequest chat = new ChatRequest(model, generateVocabularyRequest);
        QuizGenerationResult result = quizService.generateQuizAndSaveWordSet(chat, userPrincipal.getId());
        String quizId = result.getQuizId();

        return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);
    }

    /**
     * Generates a quiz based on text content from a .txt file and saves it as a vocabulary word set.
     * @param userPrincipal Current authenticated user
     * @param textToVocabRequest Request containing information about the .txt file and quiz preferences
     * @return ResponseEntity with the ID of the generated quiz or word set
     */
    @PostMapping("/generate/text-to-vocab")
    public ResponseEntity<JsonResponse> generateQuizByTextToVocab(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody TextToVocabRequest textToVocabRequest) {

        ChatRequest chat = new ChatRequest(model, textToVocabRequest);
        QuizGenerationResult result = quizService.generateQuizAndSaveWordSet(chat, userPrincipal.getId());
        String id = "";
        // If user wanted to do quiz after generation
        if(textToVocabRequest.getIsDoQuiz()){
            id = result.getQuizId();
        // if not redirect user to words page
        }else{
            id = result.getWordSetId();
        }
        return new ResponseEntity<>(new JsonResponse("success", id), HttpStatus.OK);
    }

    /**
     * Generates a quiz based on text content from a .txt file and saves it as a vocabulary word set.
     * @param userPrincipal Current authenticated user
     * @param txtVocabularyRequest Request containing information about the .txt file and quiz preferences
     * @return ResponseEntity with the ID of the generated quiz or word set
     */
    @PostMapping("/generate/txt-to-vocab")
    public ResponseEntity<JsonResponse> generateQuizByTxtToVocab(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @ModelAttribute TxtVocabularyRequest txtVocabularyRequest) {

        ChatRequest chat = new ChatRequest(model, txtVocabularyRequest);
        QuizGenerationResult result = quizService.generateQuizAndSaveWordSet(chat, userPrincipal.getId());
        String id = "";
        // If user wanted to do quiz after generation
        if(txtVocabularyRequest.getIsDoQuiz()){
            id = result.getQuizId();
            // if not redirect user to words page
        }else{
            id = result.getWordSetId();
        }
        return new ResponseEntity<>(new JsonResponse("success", id), HttpStatus.OK);
    }

    /**
     * Generates a quiz based on text content from a .doc file and saves it as a vocabulary word set.
     * @param userPrincipal Current authenticated user
     * @param docFileVocabRequest Request containing information about the .doc file and quiz preferences
     * @return ResponseEntity with the ID of the generated quiz or word set
     */
    @PostMapping("/generate/doc-to-vocab")
    public ResponseEntity<JsonResponse> generateQuizByDocToVocab(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @ModelAttribute DocFileVocabRequest docFileVocabRequest) {

        ChatRequest chat = new ChatRequest(model, docFileVocabRequest);
        QuizGenerationResult result = quizService.generateQuizAndSaveWordSet(chat, userPrincipal.getId());
        String id = "";
        // If user wanted to do quiz after generation
        if(docFileVocabRequest.getIsDoQuiz()){
            id = result.getQuizId();
            // if not redirect user to words page
        }else{
            id = result.getWordSetId();
        }
        return new ResponseEntity<>(new JsonResponse("success", id), HttpStatus.OK);
    }

    /**
     * Generates a quiz based on text content from a .pdf file and saves it as a vocabulary word set.
     * @param userPrincipal Current authenticated user
     * @param pdfFileVocabRequest Request containing information about the .pdf file and quiz preferences
     * @return ResponseEntity with the ID of the generated quiz or word set
     */
    @PostMapping("/generate/pdf-to-vocab")
    public ResponseEntity<JsonResponse> generateQuizByPdfToVocab(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @ModelAttribute PdfFileVocabRequest pdfFileVocabRequest) {

        ChatRequest chat = new ChatRequest(model, pdfFileVocabRequest);
        QuizGenerationResult result = quizService.generateQuizAndSaveWordSet(chat, userPrincipal.getId());
        String id = "";
        // If user wanted to do quiz after generation
        if(pdfFileVocabRequest.getIsDoQuiz()){
            id = result.getQuizId();
            // if not redirect user to words page
        }else{
            id = result.getWordSetId();
        }
        return new ResponseEntity<>(new JsonResponse("success", id), HttpStatus.OK);
    }

    /**
     * Deletes a quiz by its ID.
     * @param userPrincipal Current authenticated user
     * @param quizId ID of the quiz to be deleted
     * @return ResponseEntity indicating success or failure of the deletion operation
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<JsonResponse> deleteQuizById(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String quizId){
        try{
            quizService.deleteById(quizId, userPrincipal.getId());
        }catch (Exception e){
            throw new InternalErrorException(Constant.INTERNAL_ERROR_EXCEPTION_MSG);
        }

        return new ResponseEntity<>(new JsonResponse("success", "Delete success"), HttpStatus.OK);
    }
}
