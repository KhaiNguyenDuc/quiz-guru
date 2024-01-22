package com.khai.quizguru.controller;

import com.khai.quizguru.exception.AccessDeniedException;
import com.khai.quizguru.payload.response.*;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.LibraryService;
import com.khai.quizguru.service.QuizService;
import com.khai.quizguru.service.RecordService;
import com.khai.quizguru.service.UserService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final QuizService quizService;
    private final LibraryService libraryService;
    private final RecordService recordService;

    @GetMapping("/current")
    public ResponseEntity<JsonResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal){
        UserResponse user = userService.getUserById(userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", user), HttpStatus.OK);
    }

    @GetMapping("/current/quizzes")
    public ResponseEntity<JsonPageResponse<QuizResponse>> findAllQuizzesByCurrentUser(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "page", defaultValue ="0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size){
        JsonPageResponse<QuizResponse> quizzIds = quizService.findAllByUserId(userPrincipal.getId(), PageRequest.of(page, size));
        return new ResponseEntity<>(quizzIds, HttpStatus.OK);
    }


    @GetMapping("/current/quiz")
    public ResponseEntity<JsonResponse> findQuizById(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("id") String quizId){
        String user_id = userPrincipal.getId();
        QuizResponse quiz = quizService.findById(quizId);
        if(!Objects.equals(quiz.getUser().getId(), user_id)){
            throw new AccessDeniedException(Constant.ACCESS_DENIED_MSG);
        }
        return new ResponseEntity<>(new JsonResponse("success", quiz), HttpStatus.OK);
    }
    @GetMapping("/current/record")
    public ResponseEntity<JsonResponse> findRecordById(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("id") String recordId)
    {
        String user_id = userPrincipal.getId();
        RecordResponse recordResponse = recordService.findById(recordId);
        if(!Objects.equals(recordResponse.getUser().getId(), user_id)){
            throw new AccessDeniedException(Constant.ACCESS_DENIED_MSG);
        }
        return new ResponseEntity<>(new JsonResponse("success", recordResponse), HttpStatus.OK);
    }

    @GetMapping("/current/records")
    public ResponseEntity<JsonPageResponse<RecordResponse>> findAllRecordsByCurrentUser(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "page", defaultValue ="0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size){
        String user_id = userPrincipal.getId();
        JsonPageResponse<RecordResponse> records = recordService.findAllRecordsByUserId(user_id, PageRequest.of(page, size));
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/current/library")
    public ResponseEntity<JsonPageResponse<WordResponse>> findLibraryByCurrentUser(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "page", defaultValue ="0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size
    ){
        String user_id = userPrincipal.getId();
        JsonPageResponse<WordResponse> library = libraryService.findLibraryByUserId(user_id, PageRequest.of(page, size));
        return new ResponseEntity<>(library, HttpStatus.OK);
    }

}
