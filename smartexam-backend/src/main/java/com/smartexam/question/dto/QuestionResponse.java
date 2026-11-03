package com.smartexam.question.dto;

import com.smartexam.question.enums.DifficultyLevel;
import com.smartexam.question.enums.QuestionType;

public record QuestionResponse(

        Long id,

        Long organizationId,

        Long subjectId,

        String questionText,

        QuestionType questionType,

        DifficultyLevel difficulty,

        String optionA,

        String optionB,

        String optionC,

        String optionD,

        String correctAnswer,

        String explanation,

        Integer marks,

        Boolean active
) {
}