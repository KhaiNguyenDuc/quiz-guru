package com.khai.quizguru.payload.response;
import com.khai.quizguru.model.User.User;
import lombok.Data;

import java.util.List;

@Data
public class QuizResponse {

    private String id;
    private List<QuestionResponse> questions;
    private User user;
    private String givenText;
}
