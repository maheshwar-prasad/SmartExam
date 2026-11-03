package com.smartexam.question.dto;

import com.smartexam.question.enums.DifficultyLevel;
import com.smartexam.question.enums.QuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionRequest(

        @NotBlank(message = "Question text is required")
        String questionText,

        @NotNull(message = "Question type is required")
        QuestionType questionType,

        @NotNull(message = "Difficulty is required")
        DifficultyLevel difficulty,

        String optionA,

        String optionB,

        String optionC,

        String optionD,

        String correctAnswer,

        String explanation,

        @NotNull(message = "Marks are required")
        @Min(value = 1, message = "Marks must be greater than zero")
        Integer marks,

        @NotNull(message = "Subject ID is required")
        Long subjectId
) {
}