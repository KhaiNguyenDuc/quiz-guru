package com.khai.quizguru.utils;

/**
 * Includes prompt for chatgpt api
 */
public class Prompt {
    public static final String SINGLE_CHOICE_QUIZ_PROMPT = "Give me exactly %d questions of type %s about the given text which has the level %s. Return your answer entirely in the form of a JSON object. The JSON object must have a key named \"questions\" which is an array of the questions. Each quiz question should include exactly 4 choices, the answers, and a brief explanation of why the answer is correct. Don't include anything other than the JSON. The JSON properties of each question should be \"query\" (which is the question), \"choices\", \"answers\", \"type\"( \"MULTIPLE_CHOICE\" or \"SINGLE_CHOICE\") and \"explanation\". The choices shouldn't have any ordinal value like A, B, C, D or a number like 1, 2, 3, 4. The answer should be an array include the 0-indexed number of the correct choice. The json Object will returned in %s language";
    public static final String MULTIPLE_CHOICE_QUIZ_PROMPT = "Provide exactly %d questions of type %s based on the given text, with a difficulty level of %s. Return your response in the form of a JSON object. The JSON object should contain a key named \"questions,\" which is an array of the questions. Each multiple-choice question should have exactly 4 choices, answers, and a brief explanation of why the answers are correct. There must have more than 1 choice corrected, paraphrase the other choice to replicate as many correct choice as possible. The JSON properties of each question should include \"query\" (the question), \"choices,\" \"answers,\" ( always has size more than 1 ), \"type\" (use \"MULTIPLE_CHOICE\" for questions with more than 1 correct choice), and \"explanation.\" Ensure that the choices do not have any ordinal values like A, B, C, D, or numerical values like 1, 2, 3, 4. The answers should be an array including the 0-indexed numbers of correct choices. The JSON object will be returned in %s language.";
    public static final String MIX_CHOICE_QUIZ_PROMPT = "Give me exactly %d questions of type %s about the given text which has the level %s. Return your answer entirely in the form of a JSON object. The JSON object must have a key named \"questions\" which is an array of the questions. Each quiz question should include exactly 4 choices, the answers, and a brief explanation of why the answers is correct. Don't include anything other than the JSON. The JSON properties of each question should be \"query\" (which is the question), \"choices\", \"answers\", \"type\"( \"MULTIPLE_CHOICE\" for question has more than 1 correct choice or \"SINGLE_CHOICE\" for question has only 1 correct choice) and \"explanation\". The choices shouldn't have any ordinal value like A, B, C, D or a number like 1, 2, 3, 4. The answer should be the 0-indexed number of the correct choice. The json Object will returned in %s language";
    public static final String VOCABULARY_QUIZ_PROMPT = "Provide exactly %d questions of type %s based on the given vocabulary," +
            "the purpose is to help user learn about those vocabularies with a difficulty level of %s," +
            "Create the questions with context mainly about: " +
            "1. The definition of that word" +
            "2. The context that word is used ( Use cloze question - embedded answer )" +
            "3. The family words relate to that word ( Use cloze question - embedded answer)" +
            "Return your response in the form of a JSON object. The JSON object should contain a key named \"questions,\" " +
            "which is an array of the questions. Each multiple-choice question should have exactly 4 choices, answers, " +
            "and a brief explanation of why the answers are correct. There must have more than 1 choice corrected, " +
            "paraphrase the other choice to replicate as many correct choice as possible. " +
            "The JSON object should contain a key named \"words\" which is an array of given vocabularies" +
            "a key named \"questions,\" which is an array of the questions. Each multiple-choice question should have exactly 4 choices, answers, " +
            "and a brief explanation of why the answers are correct. There must have more than 1 choice corrected, " +
            "paraphrase the other choice to replicate as many correct choice as possible. " +
            "The JSON properties of each question should include \"query\" (the question), " +
            "\"choices,\" \"answers,\", \"type\" (\"MULTIPLE_CHOICE\" for question has more than 1 correct choice or \"SINGLE_CHOICE\" " +
            "for question has only 1 correct choice), and \"explanation.\" Ensure that the choices do not have any ordinal values " +
            "like A, B, C, D, or numerical values like 1, 2, 3, 4. The answers should be an array including the 0-indexed numbers " +
            "of correct choices";

    public static final String TEXT_TO_VOCAB_PROMPT = "Provide exactly %d questions of type %s based on exactly %d vocabularies you found in the given text which has the level of %s, the vocabularies should be a single word " +
            "the purpose is to help user learn about those vocabularies contain in the text, the questions should not relate to the given text but relate to the vocabularies itself" +
            "Create the questions with context mainly about: " +
            "1. The definition of that word" +
            "2. The context that word is used ( Use cloze question - embedded answer )" +
            "3. The family words relate to that word ( Use cloze question - embedded answer)" +
            "Return your response in the form of a JSON object. The JSON object should contain a key named \"words\" which is an array of vocabularies you found in the given text" +
            "a key named \"questions,\" which is an array of the questions. Each multiple-choice question should have exactly 4 choices, answers, " +
            "and a brief explanation of why the answers are correct. There must have more than 1 choice corrected, " +
            "paraphrase the other choice to replicate as many correct choice as possible. " +
            "The JSON properties of each question should include \"query\" (the question), " +
            "\"choices,\" \"answers,\", \"type\" (\"MULTIPLE_CHOICE\" for question has more than 1 correct choice or \"SINGLE_CHOICE\" " +
            "for question has only 1 correct choice), and \"explanation.\" Ensure that the choices do not have any ordinal values " +
            "like A, B, C, D, or numerical values like 1, 2, 3, 4. The answers should be an array including the 0-indexed numbers " +
            "of correct choices";

}
