package com.smartexam.ai.service.impl;

import com.smartexam.ai.dto.AiSummaryRequest;
import com.smartexam.ai.dto.AiSummaryResponse;
import com.smartexam.ai.dto.AiSummaryResult;
import com.smartexam.ai.enums.AiPromptType;
import com.smartexam.ai.service.AiEngine;
import com.smartexam.ai.service.AiSummaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiSummaryServiceImpl
        implements AiSummaryService {

    private static final int MAX_CONTENT_LENGTH = 30000;

    private final AiEngine aiEngine;

    @Override
    public AiSummaryResponse summarize(
            AiSummaryRequest request) {

        validateRequest(request);

        String prompt =
                buildPrompt(request);

        log.info(
                "Generating AI summary. topic={}, classNumber={}",
                request.topic(),
                request.classNumber()
        );

        AiSummaryResult result =
                aiEngine.generateStructured(
                        AiPromptType.SUMMARY,
                        prompt,
                        AiSummaryResult.class
                );

        validateResult(result);

        return new AiSummaryResponse(
                request.topic(),
                request.classNumber(),
                result.summary(),
                result.keyPoints(),
                result.importantTerms(),
                result.quickRevisionPoints()
        );
    }

    private String buildPrompt(
            AiSummaryRequest request) {

        String topic =
                request.topic() == null ||
                        request.topic().isBlank()
                        ? "Not specified"
                        : request.topic();

        String classNumber =
                request.classNumber() == null
                        ? "Not specified"
                        : String.valueOf(
                        request.classNumber()
                );

        return """
                Create a student-friendly study summary
                from the following study material.

                Topic:
                %s

                Class:
                %s

                STUDY MATERIAL:
                %s

                Requirements:

                1. Summarize only the information present
                   in the supplied study material.
                2. Do not invent facts.
                3. Keep the explanation suitable for school students.
                4. Explain the main concepts clearly.
                5. Extract the most important points.
                6. Identify important terms and their relevance.
                7. Create short revision points.
                8. Keep the summary concise but useful.
                9. Preserve important definitions and concepts.
                10. Return valid JSON only.

                JSON format:

                {
                  "summary": "...",
                  "keyPoints": [
                    "..."
                  ],
                  "importantTerms": [
                    "..."
                  ],
                  "quickRevisionPoints": [
                    "..."
                  ]
                }
                """.formatted(
                topic,
                classNumber,
                request.content()
        );
    }

    private void validateResult(
            AiSummaryResult result) {

        if (result == null) {

            throw new IllegalStateException(
                    "AI returned empty summary"
            );
        }

        if (result.summary() == null ||
                result.summary().isBlank()) {

            throw new IllegalStateException(
                    "AI returned empty summary"
            );
        }
    }

    private void validateRequest(
            AiSummaryRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Request is required"
            );
        }

        if (request.content() == null ||
                request.content().isBlank()) {

            throw new IllegalArgumentException(
                    "Content is required"
            );
        }

        if (request.content().length() >
                MAX_CONTENT_LENGTH) {

            throw new IllegalArgumentException(
                    "Study material is too large. Maximum allowed length is "
                            + MAX_CONTENT_LENGTH
                            + " characters"
            );
        }

        if (request.classNumber() != null &&
                request.classNumber() <= 0) {

            throw new IllegalArgumentException(
                    "Class number must be greater than zero"
            );
        }
    }
}