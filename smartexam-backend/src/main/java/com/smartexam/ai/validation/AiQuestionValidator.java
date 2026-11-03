package com.smartexam.ai.validation;

import com.smartexam.ai.dto.AiQuestionResponse;
import com.smartexam.ai.dto.AiQuestionsResponse;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AiQuestionValidator {

    public void validate(
            AiQuestionsResponse response,
            int expectedCount,
            String expectedDifficulty) {

        if (response == null) {
            throw new IllegalStateException(
                    "AI returned a null response"
            );
        }

        if (response.questions() == null) {
            throw new IllegalStateException(
                    "AI returned null questions"
            );
        }

        List<AiQuestionResponse> questions = response.questions();

        if (questions.size() != expectedCount) {
            throw new IllegalStateException(
                    "AI returned " + questions.size()
                            + " questions, expected "
                            + expectedCount
            );
        }

        for (int i = 0; i < questions.size(); i++) {

            validateQuestion(
                    questions.get(i),
                    i + 1,
                    expectedDifficulty
            );
        }
    }

    private void validateQuestion(
            AiQuestionResponse question,
            int questionNumber,
            String expectedDifficulty) {

        /*
         * Question object
         */
        if (question == null) {
            throw new IllegalStateException(
                    "Question " + questionNumber + " is null"
            );
        }

        /*
         * Question text
         */
        if (isBlank(question.questionText())) {
            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " has empty question text"
            );
        }

        /*
         * Options
         */
        validateOptions(
                question,
                questionNumber
        );

        /*
         * Correct answer
         */
        if (isBlank(question.correctAnswer())) {
            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " has no correct answer"
            );
        }

        /*
         * Correct answer must match
         * one of the options.
         *
         * Case-insensitive comparison is used
         * because AI may return:
         *
         * "chlorophyll"
         *
         * while option is:
         *
         * "Chlorophyll"
         */
        boolean correctAnswerExists =
                question.options()
                        .stream()
                        .anyMatch(option ->
                                option.trim()
                                        .equalsIgnoreCase(
                                                question.correctAnswer().trim()
                                        )
                        );

        if (!correctAnswerExists) {
            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " correct answer does not match "
                            + "any of the available options"
            );
        }

        /*
         * Explanation
         */
        if (isBlank(question.explanation())) {
            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " has empty explanation"
            );
        }

        /*
         * Difficulty
         */
        if (isBlank(question.difficulty())) {
            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " has no difficulty"
            );
        }

        if (expectedDifficulty == null
                || !question.difficulty()
                .trim()
                .equalsIgnoreCase(
                        expectedDifficulty.trim()
                )) {

            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " has invalid difficulty: "
                            + question.difficulty()
                            + ", expected: "
                            + expectedDifficulty
            );
        }

        /*
         * Marks
         */
        if (question.marks() == null
                || question.marks() <= 0) {

            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " must have positive marks"
            );
        }
    }

    private void validateOptions(
            AiQuestionResponse question,
            int questionNumber) {

        List<String> options = question.options();

        /*
         * Exactly 4 options
         */
        if (options == null || options.size() != 4) {
            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " must contain exactly 4 options"
            );
        }

        /*
         * No blank options
         */
        if (options.stream().anyMatch(this::isBlank)) {
            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " contains an empty option"
            );
        }

        /*
         * No duplicate options
         */
        Set<String> uniqueOptions = new HashSet<>(
                options.stream()
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .toList()
        );

        if (uniqueOptions.size() != options.size()) {
            throw new IllegalStateException(
                    "Question " + questionNumber
                            + " contains duplicate options"
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}