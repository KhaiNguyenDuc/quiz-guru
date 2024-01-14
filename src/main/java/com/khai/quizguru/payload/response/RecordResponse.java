package com.khai.quizguru.payload.response;

import com.khai.quizguru.model.RecordItem;
import lombok.Data;

import java.util.List;

@Data
public class RecordResponse {

    private String id;
    private String quizId;
    private List<RecordItemResponse> recordItems;
    private Integer score;
}
