package com.khai.quizguru.payload.response;

import com.khai.quizguru.model.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WordSetResponse extends UserDateAudit {
    private String id;
    private String name;
    private Integer wordNumber;
    private Integer reviewNumber;
    private String quizId;
}
