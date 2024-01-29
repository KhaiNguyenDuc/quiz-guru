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

    JsonPageResponse<WordSetResponse> findWordsById(String wordSetId, String userId, Pageable pageable);

    void deleteById(String wordSetId, String id);

    void bindQuiz(String wordSetId, String quizId, String userId);

    void addWordToWordSet(String wordSetId, WordSetRequest wordSetRequest);
    WordSetResponse updateWordSet(WordSetRequest wordSetRequest, String wordSetId, String userId);
}
