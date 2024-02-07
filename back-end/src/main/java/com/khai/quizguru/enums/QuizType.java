package com.khai.quizguru.enums;

/**
 * The QuizType enum represents different types of quizzes.
 */
public enum QuizType {
    SINGLE_CHOICE_QUESTION("SINGLE_CHOICE"),            // Represents a quiz consisting of single-choice questions
    MULTIPLE_CHOICE_QUESTION("MULTIPLE_CHOICE"),        // Represents a quiz consisting of multiple-choice questions
    MIX_QUESTION("SINGLE_CHOICE and MULTIPLE_CHOICE");  // Represents a quiz consisting of both single-choice and multiple-choice questions

    private final String value; // The string value associated with each quiz type

    /**
     * Constructs a QuizType enum constant with the specified string value.
     * @param value The string value associated with the quiz type
     */
    QuizType(String value) {
        this.value = value;
    }

    /**
     * Retrieves the string value associated with the quiz type.
     * @return The string value associated with the quiz type
     */
    public String getValue() {
        return value;
    }
}
