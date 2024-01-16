package com.khai.quizguru.enums;

public enum QuizType {
    SINGLE_CHOICE_QUESTION("SINGLE_CHOICE"),
    MULTIPLE_CHOICE_QUESTION("MULTIPLE_CHOICE"),
    MIX_QUESTION("MIX_CHOICE");

    private final String value;

    QuizType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
