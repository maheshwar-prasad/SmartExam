package com.smartexam.ai.service.impl;

import com.smartexam.ai.dto.AiAnswerEvaluationRequest;
import com.smartexam.ai.dto.AiAnswerEvaluationResponse;
import com.smartexam.ai.dto.AiAnswerEvaluationResult;
import com.smartexam.ai.enums.AiPromptType;
import com.smartexam.ai.service.AiAnswerEvaluationService;
import com.smartexam.ai.service.AiEngine;
import com.smartexam.question.entity.Question;
import com.smartexam.question.enums.QuestionType;
import com.smartexam.question.repository.QuestionRepository;
import com.smartexam.security.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiAnswerEvaluationServiceImpl implements AiAnswerEvaluationService {

    private final AiEngine aiEngine;

    private final QuestionRepository questionRepository;

    private final CurrentUserService currentUserService;

    @Override
    public AiAnswerEvaluationResponse evaluate(AiAnswerEvaluationRequest request) {

        validateRequest(request);

        Long organizationId = currentUserService.getOrganizationId();

        /*
         * -----------------------------------------------------
         * Find question with organization isolation
         * -----------------------------------------------------
         */

        Question question = questionRepository.findByIdAndOrganizationIdAndActiveTrue(request.questionId(), organizationId).orElseThrow(() -> new IllegalArgumentException("Question not found for this organization"));

        /*
         * -----------------------------------------------------
         * AI evaluation is intended for subjective questions.
         * -----------------------------------------------------
         */

        if (question.getQuestionType() == QuestionType.MCQ) {

            throw new IllegalArgumentException("AI evaluation is not required for MCQ questions");
        }

        /*
         * -----------------------------------------------------
         * Build evaluation prompt
         * -----------------------------------------------------
         */

        String prompt = buildEvaluationPrompt(question, request.studentAnswer());

        /*
         * -----------------------------------------------------
         * Call AI engine
         * -----------------------------------------------------
         */

        AiAnswerEvaluationResult result = aiEngine.generateStructured(AiPromptType.ANSWER_EVALUATION, prompt, AiAnswerEvaluationResult.class);

        /*
         * -----------------------------------------------------
         * Validate AI response
         * -----------------------------------------------------
         */

        validateEvaluationResult(result, question);

        return new AiAnswerEvaluationResponse(question.getId(), result.correct(), result.score(), result.maxScore(), result.feedback(), result.strengths(), result.improvements(), result.evaluation());
    }

    private String buildEvaluationPrompt(Question question, String studentAnswer) {

        return """
                Evaluate the student's answer to the following exam question.
                
                QUESTION:
                %s
                
                EXPECTED ANSWER:
                %s
                
                QUESTION TYPE:
                %s
                
                MAXIMUM MARKS:
                %s
                
                STUDENT ANSWER:
                %s
                
                Evaluation requirements:
                
                1. Evaluate only the student's answer against the question.
                2. Do not award marks for irrelevant information.
                3. Consider conceptual correctness.
                4. Consider important points that are missing.
                5. Do not penalize minor grammar mistakes unless they change the meaning.
                6. Award a score between 0 and the maximum marks.
                7. The score must not exceed the maximum marks.
                8. Give clear educational feedback.
                9. Mention strengths.
                10. Mention specific improvements.
                11. Return JSON only.
                
                JSON format:
                
                {
                  "correct": true,
                  "score": 4,
                  "maxScore": 5,
                  "feedback": "...",
                  "strengths": [
                    "..."
                  ],
                  "improvements": [
                    "..."
                  ],
                  "evaluation": "..."
                }
                """.formatted(question.getQuestionText(), question.getCorrectAnswer(), question.getQuestionType(), question.getMarks(), studentAnswer);
    }

    private void validateEvaluationResult(AiAnswerEvaluationResult result, Question question) {

        if (result == null) {

            throw new IllegalStateException("AI returned empty evaluation");
        }

        if (result.score() < 0) {

            throw new IllegalStateException("AI returned negative score");
        }

        double maximumMarks = question.getMarks();

        if (result.score() > maximumMarks) {

            throw new IllegalStateException("AI returned score greater than maximum marks");
        }

        if (result.maxScore() != maximumMarks) {

            throw new IllegalStateException("AI returned incorrect maximum marks");
        }

        if (result.feedback() == null || result.feedback().isBlank()) {

            throw new IllegalStateException("AI returned empty feedback");
        }
    }

    private void validateRequest(AiAnswerEvaluationRequest request) {

        if (request == null) {

            throw new IllegalArgumentException("Request is required");
        }

        if (request.questionId() == null) {

            throw new IllegalArgumentException("Question ID is required");
        }

        if (request.studentAnswer() == null || request.studentAnswer().isBlank()) {

            throw new IllegalArgumentException("Student answer is required");
        }

        if (request.studentAnswer().length() > 10000) {

            throw new IllegalArgumentException("Student answer is too long");
        }
    }
}