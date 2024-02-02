package com.khai.quizguru.exception;

public class InternalErrorException  extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = 7338611633679911108L;

    public InternalErrorException(String message) {
        super(message);
    }
}
