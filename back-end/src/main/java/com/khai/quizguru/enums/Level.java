package com.khai.quizguru.enums;
/**
 * The Level enum represents different difficulty levels for quizzes or questions.
 */
public enum Level {
    EASY("EASY"),    // Represents an easy difficulty level
    MEDIUM("MEDIUM"),  // Represents a medium difficulty level
    HARD("HARD");    // Represents a hard difficulty level

    private final String value; // The string value associated with each difficulty level

    /**
     * Constructs a Level enum constant with the specified string value.
     * @param value The string value associated with the difficulty level
     */
    Level(String value) {
        this.value = value;
    }

    /**
     * Retrieves the string value associated with the difficulty level.
     * @return The string value associated with the difficulty level
     */
    public String getValue() {
        return value;
    }
}
