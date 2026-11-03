package com.smartexam.ai.service.impl;

import com.smartexam.ai.config.AiProperties;
import com.smartexam.ai.dto.AiDocumentResponse;
import com.smartexam.ai.dto.AiQuestionGenerationRequest;
import com.smartexam.ai.dto.AiQuestionGenerationResponse;
import com.smartexam.ai.dto.AiQuestionResponse;
import com.smartexam.ai.service.AiDocumentChunker;
import com.smartexam.ai.service.AiDocumentQuestionGenerationService;
import com.smartexam.ai.service.AiDocumentService;
import com.smartexam.ai.service.AiQuestionGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiDocumentQuestionGenerationServiceImpl
        implements AiDocumentQuestionGenerationService {

    private final AiDocumentService aiDocumentService;
    private final AiDocumentChunker aiDocumentChunker;
    private final AiQuestionGenerationService aiQuestionGenerationService;
    private final AiProperties aiProperties;

    @Override
    public AiQuestionGenerationResponse generate(
            MultipartFile file,
            Integer classNumber,
            Long subjectId,
            String topic,
            String difficulty,
            Integer count,
            Boolean save) {

        if (count == null || count < 1) {
            throw new IllegalArgumentException(
                    "Question count must be greater than zero"
            );
        }

        if (count > aiProperties.getMaxQuestionsPerRequest()) {
            throw new IllegalArgumentException(
                    "Maximum questions allowed per request: "
                            + aiProperties.getMaxQuestionsPerRequest()
            );
        }

        // 1. Extract document text
        AiDocumentResponse document =
                aiDocumentService.extractText(file);

        String extractedText = document.extractedText();

        if (extractedText == null || extractedText.isBlank()) {
            throw new IllegalArgumentException(
                    "No readable text found in the uploaded document"
            );
        }

        // 2. Split document
        List<String> chunks =
                aiDocumentChunker.chunk(
                        extractedText,
                        aiProperties.getDocumentChunkSize(),
                        aiProperties.getDocumentChunkOverlap()
                );

        if (chunks.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unable to create document chunks"
            );
        }

        if (chunks.size() > aiProperties.getDocumentMaxChunks()) {
            throw new IllegalArgumentException(
                    "Document is too large. Maximum supported chunks: "
                            + aiProperties.getDocumentMaxChunks()
            );
        }

        // 3. Distribute questions across chunks
        List<Integer> questionsPerChunk =
                distributeQuestions(count, chunks.size());

        List<AiQuestionResponse> allQuestions =
                new ArrayList<>();

        List<Long> savedQuestionIds =
                new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {

            int chunkQuestionCount =
                    questionsPerChunk.get(i);

            if (chunkQuestionCount == 0) {
                continue;
            }

            AiQuestionGenerationRequest request =
                    new AiQuestionGenerationRequest(
                            classNumber,
                            subjectId,
                            topic,
                            difficulty,
                            chunkQuestionCount,
                            Boolean.TRUE.equals(save),
                            chunks.get(i)
                    );

            AiQuestionGenerationResponse response =
                    aiQuestionGenerationService.generate(request);

            if (response.questions() != null) {
                allQuestions.addAll(response.questions());
            }

            if (response.savedQuestionIds() != null) {
                savedQuestionIds.addAll(
                        response.savedQuestionIds()
                );
            }
        }

        return new AiQuestionGenerationResponse(
                allQuestions,
                Boolean.TRUE.equals(save),
                savedQuestionIds
        );
    }

    private List<Integer> distributeQuestions(
            int totalQuestions,
            int chunkCount) {

        List<Integer> distribution =
                new ArrayList<>();

        int base =
                totalQuestions / chunkCount;

        int remainder =
                totalQuestions % chunkCount;

        for (int i = 0; i < chunkCount; i++) {

            int questions =
                    base + (i < remainder ? 1 : 0);

            distribution.add(questions);
        }

        return distribution;
    }
}