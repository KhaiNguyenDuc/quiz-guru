package com.khai.quizguru.controller;

import com.khai.quizguru.exception.AccessDeniedException;
import com.khai.quizguru.payload.request.ProfileRequest;
import com.khai.quizguru.payload.response.*;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.*;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final QuizService quizService;
    private final RecordService recordService;
    private final WordSetService wordSetService;

    /**
     * Retrieves the current authenticated user.
     * @param userPrincipal Current authenticated user
     * @return ResponseEntity with the current user details
     */
    @GetMapping("/current")
    public ResponseEntity<JsonResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal){
        UserResponse user = userService.getUserById(userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", user), HttpStatus.OK);
    }

    /**
     * Updates the profile of the current authenticated user.
     * @param userPrincipal Current authenticated user
     * @param profileRequest Request containing updated profile information
     * @return ResponseEntity with the updated user details
     */
    @PutMapping("/current/update")
    public ResponseEntity<JsonResponse> updateUserProfile(
            @CurrentUser UserPrincipal userPrincipal,
            @ModelAttribute ProfileRequest profileRequest){
        UserResponse user = userService.updateById(profileRequest, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", user), HttpStatus.OK);
    }

    /**
     * Retrieves all quizzes created by the current authenticated user.
     * @param userPrincipal Current authenticated user
     * @param page Page number for pagination
     * @param size Size of each page for pagination
     * @return ResponseEntity with the list of quizzes
     */
    @GetMapping("/current/quizzes")
    public ResponseEntity<JsonPageResponse<QuizResponse>> findAllQuizzesByCurrentUser(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "page", defaultValue ="0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size){
        JsonPageResponse<QuizResponse> quizzIds = quizService.findAllByUserId(userPrincipal.getId(), PageRequest.of(page, size));
        return new ResponseEntity<>(quizzIds, HttpStatus.OK);
    }

    /**
     * Retrieves the quiz with the specified ID for the current authenticated user.
     * @param userPrincipal Current authenticated user
     * @param quizId ID of the quiz to retrieve
     * @return ResponseEntity with the quiz details
     */
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

    /**
     * Retrieves the record with the specified ID for the current authenticated user.
     * @param userPrincipal Current authenticated user
     * @param recordId ID of the record to retrieve
     * @return ResponseEntity with the record details
     */
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

    /**
     * Retrieves all records belonging to the current authenticated user.
     * @param userPrincipal Current authenticated user
     * @param page Page number for pagination
     * @param size Size of each page for pagination
     * @return ResponseEntity with the list of records
     */
    @GetMapping("/current/records")
    public ResponseEntity<JsonPageResponse<RecordResponse>> findAllRecordsByCurrentUser(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "page", defaultValue ="0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size){
        String user_id = userPrincipal.getId();
        JsonPageResponse<RecordResponse> records = recordService.findAllRecordsByUserId(user_id, PageRequest.of(page, size));
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    /**
     * Retrieves all word sets created by the current authenticated user.
     * @param userPrincipal Current authenticated user
     * @param page Page number for pagination
     * @param size Size of each page for pagination
     * @return ResponseEntity with the list of word sets
     */
    @GetMapping("/current/word-set")
    public ResponseEntity<JsonPageResponse<WordSetResponse>> findAllWordSetByCurrentUser(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "page", defaultValue ="0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size
    ){
        String user_id = userPrincipal.getId();
        JsonPageResponse<WordSetResponse> wordSetResponse = wordSetService.findAllWordSetByUserId(user_id, PageRequest.of(page, size));
        return new ResponseEntity<>(wordSetResponse, HttpStatus.OK);
    }

}
