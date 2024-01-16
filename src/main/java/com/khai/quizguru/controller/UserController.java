package com.khai.quizguru.controller;

import com.khai.quizguru.exception.AccessDeniedException;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.payload.response.QuizResponse;
import com.khai.quizguru.payload.response.RecordResponse;
import com.khai.quizguru.payload.response.UserResponse;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.QuizService;
import com.khai.quizguru.service.RecordService;
import com.khai.quizguru.service.UserService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
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
    private final RecordService recordService;

    @GetMapping("/current")
    public ResponseEntity<JsonResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal){
        UserResponse user = userService.getUserById(userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", user), HttpStatus.OK);
    }

    @GetMapping("/current/quizzes")
    public ResponseEntity<JsonResponse> findAllByCurrentUser(@CurrentUser UserPrincipal userPrincipal){
        List<QuizResponse> quizzIds = quizService.findAllByUserId(userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", quizzIds), HttpStatus.OK);
    }


    @GetMapping("/current/quiz")
    public ResponseEntity<JsonResponse> findQuizById(@CurrentUser UserPrincipal userPrincipal,
                                                 @RequestParam("id") String quizId){
        String user_id = userPrincipal.getId();
        QuizResponse quiz = quizService.findById(quizId);
        if(!Objects.equals(quiz.getUser().getId(), user_id)){
            throw new AccessDeniedException(Constant.ACCESS_DENIED_MSG);
        }
        return new ResponseEntity<>(new JsonResponse("success", quiz), HttpStatus.OK);
    }
    @GetMapping("/current/record")
    public ResponseEntity<JsonResponse> findRecordById(@CurrentUser UserPrincipal userPrincipal,
                                                 @RequestParam("id") String recordId){
        String user_id = userPrincipal.getId();
        RecordResponse recordResponse = recordService.findById(recordId);
        if(!Objects.equals(recordResponse.getUser().getId(), user_id)){
            throw new AccessDeniedException(Constant.ACCESS_DENIED_MSG);
        }
        return new ResponseEntity<>(new JsonResponse("success", recordResponse), HttpStatus.OK);
    }

    @GetMapping("/current/records")
    public ResponseEntity<JsonResponse> findAllRecordsByCurrentUser(@CurrentUser UserPrincipal userPrincipal){
        String user_id = userPrincipal.getId();
        List<RecordResponse> records = recordService.findAllRecordsByUserId(user_id);
        return new ResponseEntity<>(new JsonResponse("success", records), HttpStatus.OK);
    }
}
