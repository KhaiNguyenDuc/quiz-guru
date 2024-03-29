package com.khai.quizguru.payload.response;

import com.khai.quizguru.model.user.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private List<Role> roles;
    private String imagePath;

}
