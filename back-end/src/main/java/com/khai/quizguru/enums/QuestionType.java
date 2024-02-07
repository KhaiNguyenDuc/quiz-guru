package com.khai.quizguru.enums;

/**
 * The QuestionType enum represents different types of quiz questions.
 */
public enum QuestionType {
    SINGLE_CHOICE("SINGLE_CHOICE"),    // Represents a single-choice question type
    MULTIPLE_CHOICE("MULTIPLE_CHOICE");  // Represents a multiple-choice question type

    private final String value; // The string value associated with each question type

    /**
     * Constructs a QuestionType enum constant with the specified string value.
     * @param value The string value associated with the question type
     */
    QuestionType(String value) {
        this.value = value;
    }

    /**
     * Retrieves the string value associated with the question type.
     * @return The string value associated with the question type
     */
    public String getValue() {
        return value;
    }
}
