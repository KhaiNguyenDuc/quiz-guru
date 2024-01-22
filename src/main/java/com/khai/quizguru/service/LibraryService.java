package com.khai.quizguru.service;

import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.LibraryResponse;
import com.khai.quizguru.payload.response.WordResponse;
import org.springframework.data.domain.Pageable;

public interface LibraryService {
    JsonPageResponse<WordResponse> findLibraryByUserId(String userId, Pageable pageable);
}
