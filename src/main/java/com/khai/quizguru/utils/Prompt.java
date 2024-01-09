package com.khai.quizguru.utils;

public class Prompt {
    public static final String GENERATE_QUIZ_PROMPT = "Give me exactly %d questions of type %s about the given text which has the level %s by %s language. Return your answer entirely in the form of a JSON object. The JSON object should have a key named \"questions\" which is an array of the questions. Each quiz question should include exactly 4 choices, the answer, and a brief explanation of why the answer is correct. Don't include anything other than the JSON. The JSON properties of each question should be \"query\" (which is the question), \"choices\", \"answer\", and \"explanation\". The choices shouldn't have any ordinal value like A, B, C, D or a number like 1, 2, 3, 4. The answer should be the 0-indexed number of the correct choice.";

}
