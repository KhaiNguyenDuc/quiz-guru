package com.khai.quizguru.payload.response;

import com.khai.quizguru.model.RecordItem;
import com.khai.quizguru.model.User.User;
import com.khai.quizguru.model.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordResponse extends UserDateAudit {

    private String id;
    private String quizId;
    private List<RecordItemResponse> recordItems;
    private Integer score;
    private User user;
    private String givenText;
}
