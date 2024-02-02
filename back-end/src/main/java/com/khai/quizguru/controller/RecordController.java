package com.khai.quizguru.controller;

import com.khai.quizguru.payload.request.RecordRequest;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.payload.response.RecordResponse;
import com.khai.quizguru.security.CurrentUser;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
@Slf4j
public class RecordController {

    private final RecordService recordService;
    @PostMapping("/create")
    public ResponseEntity<JsonResponse> createRecord(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody RecordRequest recordRequest){
        log.info(recordRequest.getQuizId());
        RecordResponse recordResponse = recordService.createRecord(recordRequest, userPrincipal.getId());
        return new ResponseEntity<>(new JsonResponse("success", recordResponse), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<JsonResponse> getRecordById(@PathVariable("id") String recordId){
        RecordResponse recordResponse = recordService.findById(recordId);
        return new ResponseEntity<>(new JsonResponse("success", recordResponse), HttpStatus.OK);
    }
}
