package com.khai.quizguru.exception;

public class TokenRefreshException extends RuntimeException {
    private static final long serialVersionUID = -7738023113292649316L;

    public TokenRefreshException(String message) {
        super(message);
    }

}
