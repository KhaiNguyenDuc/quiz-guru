package com.khai.quizguru.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class ExceptionDetails {

    private Date timestamp;
    private String message;
    private String details;
}