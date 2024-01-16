package com.khai.quizguru.payload.response;
import com.khai.quizguru.model.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuizResponse extends UserDateAudit {

    private String id;
    private List<QuestionResponse> questions;
    private UserResponse user;
    private String givenText;
    public String type;
    public Integer number;
    public String language;
    public String level;
    public Integer duration;
}
