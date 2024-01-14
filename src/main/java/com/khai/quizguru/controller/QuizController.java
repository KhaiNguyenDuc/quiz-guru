package com.khai.quizguru.controller;

import com.khai.quizguru.Exception.InvalidRequestException;
import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.payload.request.Prompt.BaseTextRequest;
import com.khai.quizguru.payload.request.Prompt.DocFileRequest;
import com.khai.quizguru.payload.request.Prompt.PdfFileRequest;
import com.khai.quizguru.payload.request.Prompt.TxtFileRequest;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.payload.response.QuizResponse;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.QuizService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizController {


    private final QuizService quizService;

    @Value("${openai.model}")
    private String model;

    @PostMapping("/generate/txt")
    public ResponseEntity<JsonResponse> generateQuizByTxtFile(
            @CurrentUser UserPrincipal userPrincipal,
            @ModelAttribute TxtFileRequest txtFileRequest) {

        ChatRequest chat = new ChatRequest(model, txtFileRequest);
        String quizId = quizService.generateQuiz(chat, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);

    }

    @PostMapping("/generate/pdf")
    public ResponseEntity<JsonResponse> generateQuizByPdfFile(
            @CurrentUser UserPrincipal userPrincipal,
            @ModelAttribute PdfFileRequest pdfFileRequest) {

        ChatRequest chat = new ChatRequest(model, pdfFileRequest);
        String quizId = quizService.generateQuiz(chat, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);

    }

    @PostMapping("/generate/doc")
    public ResponseEntity<JsonResponse> generateQuizByDocFile(
            @CurrentUser UserPrincipal userPrincipal,
            @ModelAttribute DocFileRequest docFileRequest) {

        ChatRequest chat = new ChatRequest(model, docFileRequest);
        String quizId = quizService.generateQuiz(chat, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);

    }

    @PostMapping("/generate/text")
    public ResponseEntity<JsonResponse> generateQuizByText(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody BaseTextRequest baseTextRequest) {

            ChatRequest chat = new ChatRequest(model, baseTextRequest);
            String quizId = quizService.generateQuiz(chat, userPrincipal.getId());
            return new ResponseEntity<>(new JsonResponse("success", quizId), HttpStatus.OK);


    }
}
