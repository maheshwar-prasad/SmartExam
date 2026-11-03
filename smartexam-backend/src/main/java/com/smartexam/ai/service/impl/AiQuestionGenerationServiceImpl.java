package com.smartexam.ai.service.impl;

import com.smartexam.ai.config.AiProperties;
import com.smartexam.ai.dto.AiQuestionGenerationRequest;
import com.smartexam.ai.dto.AiQuestionGenerationResponse;
import com.smartexam.ai.dto.AiQuestionResponse;
import com.smartexam.ai.dto.AiQuestionsResponse;
import com.smartexam.ai.enums.AiPromptType;
import com.smartexam.ai.service.AiEngine;
import com.smartexam.ai.service.AiQuestionGenerationService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.question.entity.Question;
import com.smartexam.question.enums.DifficultyLevel;
import com.smartexam.question.enums.QuestionType;
import com.smartexam.question.repository.QuestionRepository;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.subject.entity.Subject;
import com.smartexam.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiQuestionGenerationServiceImpl
        implements AiQuestionGenerationService {

    private final AiEngine aiEngine;

    private final AiProperties aiProperties;

    private final SubjectRepository subjectRepository;

    private final QuestionRepository questionRepository;

    private final CurrentUserService currentUserService;

    @Override
    public AiQuestionGenerationResponse generate(
            AiQuestionGenerationRequest request) {

        validateRequest(request);

        Long organizationId =
                currentUserService.getOrganizationId();

        log.info(
                "AI question generation started. organizationId={}, subjectId={}, topic={}, count={}, documentMode={}",
                organizationId,
                request.subjectId(),
                request.topic(),
                request.count(),
                hasSourceContent(request)
        );

        /*
         * -------------------------------------------------------
         * 1. Get subject using organization isolation
         * -------------------------------------------------------
         */

        Subject subject =
                subjectRepository
                        .findByIdAndOrganizationIdAndActiveTrue(
                                request.subjectId(),
                                organizationId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Subject not found for this organization"
                                )
                        );

        /*
         * -------------------------------------------------------
         * 2. Generate questions using AI
         *
         * IMPORTANT:
         * Only AI generation is retried.
         * Database saving happens AFTER successful generation.
         * -------------------------------------------------------
         */

        List<AiQuestionResponse> aiQuestions =
                generateWithRetry(
                        request,
                        subject
                );

        /*
         * -------------------------------------------------------
         * 3. Validate generated questions
         * -------------------------------------------------------
         */

        validateGeneratedQuestions(
                aiQuestions,
                request
        );

        /*
         * -------------------------------------------------------
         * 4. Remove duplicate questions
         * -------------------------------------------------------
         */

        validateNoDuplicateQuestions(aiQuestions);

        /*
         * -------------------------------------------------------
         * 5. Return without saving
         * -------------------------------------------------------
         */

        if (!Boolean.TRUE.equals(request.save())) {

            return new AiQuestionGenerationResponse(
                    aiQuestions,
                    false,
                    List.of()
            );
        }

        /*
         * -------------------------------------------------------
         * 6. Save generated questions
         *
         * IMPORTANT:
         * This is outside the AI retry logic.
         * -------------------------------------------------------
         */

        Organization organization =
                currentUserService
                        .getCurrentUser()
                        .getUser()
                        .getOrganization();

        List<Long> savedQuestionIds =
                saveQuestions(
                        aiQuestions,
                        subject,
                        organization
                );

        log.info(
                "AI questions saved successfully. organizationId={}, count={}",
                organizationId,
                savedQuestionIds.size()
        );

        return new AiQuestionGenerationResponse(
                aiQuestions,
                true,
                savedQuestionIds
        );
    }

    /**
     * Generates questions using AI.
     *
     * Only AI generation is retried.
     * Database operations are never performed inside this retry loop.
     */
    private List<AiQuestionResponse> generateWithRetry(
            AiQuestionGenerationRequest request,
            Subject subject) {

        String userPrompt =
                buildUserPrompt(
                        request,
                        subject
                );

        int maxRetry =
                Math.max(
                        1,
                        aiProperties.getMaxRetry()
                );

        Exception lastException = null;

        for (int attempt = 1;
             attempt <= maxRetry;
             attempt++) {

            try {

                log.info(
                        "AI generation attempt {}/{}",
                        attempt,
                        maxRetry
                );

                AiQuestionsResponse response =
                        aiEngine.generateStructured(
                                AiPromptType.QUESTION_GENERATION,
                                userPrompt,
                                AiQuestionsResponse.class
                        );

                if (response == null ||
                        response.questions() == null ||
                        response.questions().isEmpty()) {

                    throw new IllegalStateException(
                            "AI returned no questions"
                    );
                }

                return response.questions();

            } catch (Exception e) {

                lastException = e;

                log.warn(
                        "AI generation attempt {} failed",
                        attempt,
                        e
                );
            }
        }

        throw new IllegalStateException(
                "Failed to generate AI questions after "
                        + maxRetry
                        + " attempts",
                lastException
        );
    }

    /**
     * Builds the user prompt.
     *
     * Supports both:
     *
     * 1. Normal AI question generation
     * 2. Document-based AI question generation
     */
    private String buildUserPrompt(
            AiQuestionGenerationRequest request,
            Subject subject) {

        StringBuilder prompt =
                new StringBuilder();

        prompt.append("""
                Generate exactly %d multiple-choice questions.

                Class: %d
                Subject: %s
                Topic: %s
                Difficulty: %s

                Requirements:

                1. Generate exactly %d questions.
                2. Every question must be an MCQ.
                3. Each question must have exactly 4 options.
                4. There must be exactly one correct answer.
                5. correctAnswer must exactly match one of the options.
                6. Difficulty must be %s.
                7. Marks must be a positive integer.
                8. Provide a clear explanation for every answer.
                9. Questions must be suitable for class %d students.
                10. Do not duplicate questions.
                11. Do not include markdown.
                12. Return valid JSON only.

                JSON format:

                {
                  "questions": [
                    {
                      "questionText": "...",
                      "options": [
                        "...",
                        "...",
                        "...",
                        "..."
                      ],
                      "correctAnswer": "...",
                      "explanation": "...",
                      "difficulty": "%s",
                      "marks": 1
                    }
                  ]
                }
                """.formatted(
                request.count(),
                request.classNumber(),
                subject.getName(),
                request.topic(),
                request.difficulty(),
                request.count(),
                request.difficulty(),
                request.classNumber(),
                request.difficulty()
        ));

        /*
         * -------------------------------------------------------
         * Document-based generation
         * -------------------------------------------------------
         */

        if (hasSourceContent(request)) {

            prompt.append("""

                    IMPORTANT SOURCE-MATERIAL RULES:

                    The following source material was provided by the
                    teacher.

                    Generate questions ONLY from the information contained
                    in this source material.

                    Do NOT introduce facts that are not supported by the
                    source material.

                    Do NOT use outside knowledge.

                    Questions should test understanding of the provided
                    material and should be appropriate for the requested
                    class and difficulty.

                    SOURCE MATERIAL:
                    ====================

                    """);

            prompt.append(request.sourceContent());

            prompt.append("""

                    ====================
                    END SOURCE MATERIAL

                    Return valid JSON only.
                    """);
        }

        return prompt.toString();
    }

    private boolean hasSourceContent(
            AiQuestionGenerationRequest request) {

        return request.sourceContent() != null
                && !request.sourceContent().isBlank();
    }

    private void validateGeneratedQuestions(
            List<AiQuestionResponse> questions,
            AiQuestionGenerationRequest request) {

        if (questions == null ||
                questions.size() != request.count()) {

            throw new IllegalStateException(
                    "AI returned "
                            + (questions == null
                            ? 0
                            : questions.size())
                            + " questions instead of "
                            + request.count()
            );
        }

        for (int i = 0; i < questions.size(); i++) {

            AiQuestionResponse question =
                    questions.get(i);

            validateQuestion(
                    question,
                    i + 1,
                    request
            );
        }
    }

    private void validateQuestion(
            AiQuestionResponse question,
            int questionNumber,
            AiQuestionGenerationRequest request) {

        if (question == null) {

            throw new IllegalStateException(
                    "Generated question "
                            + questionNumber
                            + " is null"
            );
        }

        if (question.questionText() == null ||
                question.questionText().isBlank()) {

            throw new IllegalStateException(
                    "Question "
                            + questionNumber
                            + " has empty question text"
            );
        }

        if (question.options() == null ||
                question.options().size() != 4) {

            throw new IllegalStateException(
                    "Question "
                            + questionNumber
                            + " must have exactly 4 options"
            );
        }

        for (String option : question.options()) {

            if (option == null ||
                    option.isBlank()) {

                throw new IllegalStateException(
                        "Question "
                                + questionNumber
                                + " contains an empty option"
                );
            }
        }

        if (question.correctAnswer() == null ||
                question.correctAnswer().isBlank()) {

            throw new IllegalStateException(
                    "Question "
                            + questionNumber
                            + " has no correct answer"
            );
        }

        boolean correctAnswerExists =
                question.options()
                        .stream()
                        .anyMatch(option ->
                                option.equals(
                                        question.correctAnswer()
                                )
                        );

        if (!correctAnswerExists) {

            throw new IllegalStateException(
                    "Question "
                            + questionNumber
                            + " correctAnswer does not match any option"
            );
        }

        if (question.explanation() == null ||
                question.explanation().isBlank()) {

            throw new IllegalStateException(
                    "Question "
                            + questionNumber
                            + " has no explanation"
            );
        }

        if (question.marks() == null ||
                question.marks() <= 0) {

            throw new IllegalStateException(
                    "Question "
                            + questionNumber
                            + " must have positive marks"
            );
        }

        if (question.difficulty() == null ||
                question.difficulty().isBlank()) {

            throw new IllegalStateException(
                    "Question "
                            + questionNumber
                            + " has no difficulty"
            );
        }

        if (!question.difficulty()
                .trim()
                .equalsIgnoreCase(
                        request.difficulty().trim()
                )) {

            throw new IllegalStateException(
                    "Question "
                            + questionNumber
                            + " has incorrect difficulty"
            );
        }
    }

    /**
     * Detect duplicate questions generated within this request.
     */
    private void validateNoDuplicateQuestions(
            List<AiQuestionResponse> questions) {

        Set<String> uniqueQuestions =
                new HashSet<>();

        for (AiQuestionResponse question : questions) {

            String normalized =
                    normalizeQuestion(
                            question.questionText()
                    );

            if (!uniqueQuestions.add(normalized)) {

                throw new IllegalStateException(
                        "AI generated duplicate question: "
                                + question.questionText()
                );
            }
        }
    }

    private String normalizeQuestion(
            String question) {

        if (question == null) {
            return "";
        }

        return question
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", " ");
    }

    @Transactional
    protected List<Long> saveQuestions(
            List<AiQuestionResponse> aiQuestions,
            Subject subject,
            Organization organization) {

        List<Question> questions =
                new ArrayList<>();

        for (AiQuestionResponse aiQuestion :
                aiQuestions) {

            DifficultyLevel difficulty =
                    DifficultyLevel.valueOf(
                            aiQuestion.difficulty()
                                    .trim()
                                    .toUpperCase()
                    );

            List<String> options =
                    aiQuestion.options();

            Question question =
                    Question.builder()
                            .questionText(
                                    aiQuestion.questionText()
                            )
                            .questionType(
                                    QuestionType.MCQ
                            )
                            .difficulty(
                                    difficulty
                            )
                            .optionA(
                                    options.get(0)
                            )
                            .optionB(
                                    options.get(1)
                            )
                            .optionC(
                                    options.get(2)
                            )
                            .optionD(
                                    options.get(3)
                            )
                            .correctAnswer(
                                    aiQuestion.correctAnswer()
                            )
                            .explanation(
                                    aiQuestion.explanation()
                            )
                            .marks(
                                    aiQuestion.marks()
                            )
                            .organization(
                                    organization
                            )
                            .subject(
                                    subject
                            )
                            .active(true)
                            .build();

            questions.add(question);
        }

        List<Question> savedQuestions =
                questionRepository.saveAll(
                        questions
                );

        return savedQuestions
                .stream()
                .map(Question::getId)
                .toList();
    }

    private void validateRequest(
            AiQuestionGenerationRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Request is required"
            );
        }

        if (request.classNumber() == null ||
                request.classNumber() <= 0) {

            throw new IllegalArgumentException(
                    "Class number must be greater than zero"
            );
        }

        if (request.count() == null ||
                request.count() <= 0) {

            throw new IllegalArgumentException(
                    "Question count must be greater than zero"
            );
        }

        if (request.count() >
                aiProperties.getMaxQuestionsPerRequest()) {

            throw new IllegalArgumentException(
                    "Maximum "
                            + aiProperties.getMaxQuestionsPerRequest()
                            + " questions can be generated in one request"
            );
        }

        if (request.subjectId() == null) {

            throw new IllegalArgumentException(
                    "Subject ID is required"
            );
        }

        if (request.topic() == null ||
                request.topic().isBlank()) {

            throw new IllegalArgumentException(
                    "Topic is required"
            );
        }

        if (request.difficulty() == null ||
                request.difficulty().isBlank()) {

            throw new IllegalArgumentException(
                    "Difficulty is required"
            );
        }

        if (hasSourceContent(request)
                && request.sourceContent().length()
                > aiProperties.getMaxPromptLength()) {

            throw new IllegalArgumentException(
                    "Source content is too large for a single AI request. "
                            + "Use document chunking before question generation."
            );
        }
    }
}