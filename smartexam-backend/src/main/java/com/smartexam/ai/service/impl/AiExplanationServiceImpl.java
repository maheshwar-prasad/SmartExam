package com.smartexam.ai.service.impl;

import com.smartexam.ai.dto.AiExplanationRequest;
import com.smartexam.ai.dto.AiExplanationResponse;
import com.smartexam.ai.dto.AiExplanationResult;
import com.smartexam.ai.enums.AiPromptType;
import com.smartexam.ai.service.AiEngine;
import com.smartexam.ai.service.AiExplanationService;
import com.smartexam.question.entity.Question;
import com.smartexam.question.repository.QuestionRepository;
import com.smartexam.security.service.CurrentUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiExplanationServiceImpl
        implements AiExplanationService {

    private final AiEngine aiEngine;

    private final QuestionRepository questionRepository;

    private final CurrentUserService currentUserService;

    @Override
    public AiExplanationResponse explain(
            AiExplanationRequest request) {

        validateRequest(request);

        Long organizationId =
                currentUserService.getOrganizationId();

        /*
         * --------------------------------------------------
         * Load question with organization isolation
         * --------------------------------------------------
         */

        Question question =
                questionRepository
                        .findByIdAndOrganizationIdAndActiveTrue(
                                request.questionId(),
                                organizationId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Question not found for this organization"
                                )
                        );

        /*
         * --------------------------------------------------
         * Build AI prompt
         * --------------------------------------------------
         */

        String prompt =
                buildPrompt(
                        question,
                        request.studentAnswer()
                );

        /*
         * --------------------------------------------------
         * Generate structured explanation
         * --------------------------------------------------
         */

        AiExplanationResult result =
                aiEngine.generateStructured(
                        AiPromptType.QUESTION_EXPLANATION,
                        prompt,
                        AiExplanationResult.class
                );

        /*
         * --------------------------------------------------
         * Validate AI response
         * --------------------------------------------------
         */

        validateResult(result);

        return new AiExplanationResponse(
                question.getId(),
                question.getQuestionText(),
                result.concept(),
                result.explanation(),
                result.correctAnswerExplanation(),
                result.studentAnswerExplanation(),
                result.keyPoints(),
                result.commonMistakes()
        );
    }

    private String buildPrompt(
            Question question,
            String studentAnswer) {

        String studentAnswerText =
                studentAnswer == null ||
                        studentAnswer.isBlank()
                        ? "No student answer was provided."
                        : studentAnswer;

        return """
                Explain the following exam question
                to a student in a clear and educational way.

                QUESTION:
                %s

                QUESTION TYPE:
                %s

                CORRECT ANSWER:
                %s

                EXISTING EXPLANATION:
                %s

                STUDENT ANSWER:
                %s

                Requirements:

                1. Explain the underlying concept clearly.
                2. Use language appropriate for school students.
                3. Explain why the correct answer is correct.
                4. If a student answer is provided, explain whether
                   it is correct or incorrect.
                5. Explain the student's mistake if applicable.
                6. Do not invent information unrelated to the question.
                7. Provide important key points.
                8. Mention common mistakes students make.
                9. Use simple examples where helpful.
                10. Return valid JSON only.

                JSON format:

                {
                  "concept": "...",
                  "explanation": "...",
                  "correctAnswerExplanation": "...",
                  "studentAnswerExplanation": "...",
                  "keyPoints": [
                    "..."
                  ],
                  "commonMistakes": [
                    "..."
                  ]
                }
                """.formatted(
                question.getQuestionText(),
                question.getQuestionType(),
                question.getCorrectAnswer(),
                question.getExplanation() == null
                        ? "No existing explanation available."
                        : question.getExplanation(),
                studentAnswerText
        );
    }

    private void validateResult(
            AiExplanationResult result) {

        if (result == null) {
            throw new IllegalStateException(
                    "AI returned empty explanation"
            );
        }

        if (result.explanation() == null ||
                result.explanation().isBlank()) {

            throw new IllegalStateException(
                    "AI returned empty explanation"
            );
        }
    }

    private void validateRequest(
            AiExplanationRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Request is required"
            );
        }

        if (request.questionId() == null) {
            throw new IllegalArgumentException(
                    "Question ID is required"
            );
        }

        if (request.studentAnswer() != null &&
                request.studentAnswer().length() > 10000) {

            throw new IllegalArgumentException(
                    "Student answer is too long"
            );
        }
    }
}