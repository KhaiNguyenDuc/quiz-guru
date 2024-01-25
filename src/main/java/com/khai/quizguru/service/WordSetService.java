package com.khai.quizguru.service;

import com.khai.quizguru.payload.request.WordSetRequest;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.payload.response.WordSetResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WordSetService {
    String createWordSet(WordSetRequest wordSetRequest, String id);

    JsonPageResponse<WordSetResponse> findAllWordSetByUserId(String userId, Pageable pageable);

    JsonPageResponse<WordResponse> findWordsById(String wordSetId, String userId, Pageable pageable);

    void deleteById(String wordSetId, String id);
}
