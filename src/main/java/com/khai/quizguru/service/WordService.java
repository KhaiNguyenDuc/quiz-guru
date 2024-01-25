package com.khai.quizguru.service;

import java.util.List;

public interface WordService {
    List<Object> findDefinition(List<String> words);
}
