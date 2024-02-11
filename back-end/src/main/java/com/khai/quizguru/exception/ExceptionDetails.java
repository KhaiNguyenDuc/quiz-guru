package com.khai.quizguru.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class ExceptionDetails {

    private Date timestamp;
    private Object message;
    private String details;
}