package com.khai.quizguru.service;

import com.khai.quizguru.payload.request.RecordRequest;
import com.khai.quizguru.payload.response.RecordResponse;

import java.util.List;

public interface RecordService {
    List<RecordResponse> findAllRecordsByUserId(String userId);

    RecordResponse createRecord(RecordRequest recordRequest, String id);

    RecordResponse findById(String recordId);
}
