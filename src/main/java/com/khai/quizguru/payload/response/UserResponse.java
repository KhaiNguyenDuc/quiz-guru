package com.khai.quizguru.payload.response;

import com.khai.quizguru.model.User.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private String id;
    private String username;
    private List<Role> roles;

}
