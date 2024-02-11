package com.khai.quizguru.payload.request;

import com.khai.quizguru.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;

    @Size(min = 7, message = Constant.INVALID_PASSWORD_MSG)
    @NotBlank(message = "Password can't be empty")
    private String password;

    @NotBlank(message = "Token can't be empty")
    private String token;
}
