package com.khai.quizguru.payload.response;

import com.khai.quizguru.model.user.Role;
import lombok.Data;

import java.util.List;

@Data
public class RegisterResponse {

    private String id;
    private String username;
    private String email;
    private List<Role> roles;
}
