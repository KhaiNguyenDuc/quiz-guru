package com.khai.quizguru.service;

import com.khai.quizguru.model.Word;
import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.response.WordResponse;

import java.util.List;

public interface WordService {
    List<WordResponse> findDefinition(String wordSetId, List<String> words, String userId);

    WordResponse udpateWordDefinition(String wordId, WordRequest wordRequest, String id);
}
