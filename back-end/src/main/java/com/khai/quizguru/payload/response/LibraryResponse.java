package com.khai.quizguru.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class LibraryResponse {
    private List<WordResponse> wordRespons;
}
