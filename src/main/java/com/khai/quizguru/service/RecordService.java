package com.khai.quizguru.service;

import com.khai.quizguru.payload.request.RecordRequest;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.RecordResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecordService {
    JsonPageResponse<RecordResponse> findAllRecordsByUserId(String userId, Pageable pageable);

    RecordResponse createRecord(RecordRequest recordRequest, String id);

    RecordResponse findById(String recordId);
}
