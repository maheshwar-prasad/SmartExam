package com.smartexam.ai.service.impl;

import com.smartexam.ai.config.AiProperties;
import com.smartexam.ai.dto.AiDocumentResponse;
import com.smartexam.ai.dto.AiSummaryRequest;
import com.smartexam.ai.dto.AiSummaryResponse;
import com.smartexam.ai.service.AiDocumentChunker;
import com.smartexam.ai.service.AiDocumentService;
import com.smartexam.ai.service.AiDocumentSummaryService;
import com.smartexam.ai.service.AiSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiDocumentSummaryServiceImpl
        implements AiDocumentSummaryService {

    private final AiDocumentService aiDocumentService;
    private final AiDocumentChunker aiDocumentChunker;
    private final AiSummaryService aiSummaryService;
    private final AiProperties aiProperties;

    @Override
    public AiSummaryResponse summarize(
            MultipartFile file,
            String topic,
            Integer classNumber) {

        // 1. Extract text
        AiDocumentResponse document =
                aiDocumentService.extractText(file);

        String extractedText = document.extractedText();

        if (extractedText == null || extractedText.isBlank()) {
            throw new IllegalArgumentException(
                    "No readable text found in the uploaded document"
            );
        }

        // 2. Split document into chunks
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

        // 3. Summarize every chunk
        List<AiSummaryResponse> chunkSummaries =
                new ArrayList<>();

        for (String chunk : chunks) {

            AiSummaryRequest chunkRequest =
                    new AiSummaryRequest(
                            chunk,
                            topic,
                            classNumber
                    );

            AiSummaryResponse summary =
                    aiSummaryService.summarize(chunkRequest);

            chunkSummaries.add(summary);
        }

        // 4. Combine chunk summaries
        String combinedSummaries =
                buildCombinedSummary(chunkSummaries);

        // 5. Generate final summary
        AiSummaryRequest finalRequest =
                new AiSummaryRequest(
                        combinedSummaries,
                        topic,
                        classNumber
                );

        return buildFinalSummary(
                chunkSummaries,
                topic,
                classNumber
        );
    }

    private String buildCombinedSummary(
            List<AiSummaryResponse> summaries) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < summaries.size(); i++) {

            AiSummaryResponse summary = summaries.get(i);

            builder.append("SECTION ")
                    .append(i + 1)
                    .append("\n");

            builder.append("Summary:\n")
                    .append(summary.summary())
                    .append("\n\n");

            builder.append("Key Points:\n");

            if (summary.keyPoints() != null) {
                summary.keyPoints()
                        .forEach(point ->
                                builder.append("- ")
                                        .append(point)
                                        .append("\n")
                        );
            }

            builder.append("\nImportant Terms:\n");

            if (summary.importantTerms() != null) {
                summary.importantTerms()
                        .forEach(term ->
                                builder.append("- ")
                                        .append(term)
                                        .append("\n")
                        );
            }

            builder.append("\nQuick Revision:\n");

            if (summary.quickRevisionPoints() != null) {
                summary.quickRevisionPoints()
                        .forEach(point ->
                                builder.append("- ")
                                        .append(point)
                                        .append("\n")
                        );
            }

            builder.append("\n-------------------------\n\n");
        }
        return builder.toString();
    }

    private AiSummaryResponse buildFinalSummary(
            List<AiSummaryResponse> summaries,
            String topic,
            Integer classNumber) {

        List<String> currentLevel =
                summaries.stream()
                        .map(this::toSummaryText)
                        .toList();

        while (currentLevel.size() > 1) {

            List<String> nextLevel = new ArrayList<>();

            StringBuilder batch = new StringBuilder();

            for (String summary : currentLevel) {

                if (batch.length() + summary.length()
                        > aiProperties.getMaxPromptLength()
                        && !batch.isEmpty()) {

                    nextLevel.add(
                            summarizeIntermediate(
                                    batch.toString(),
                                    topic,
                                    classNumber
                            )
                    );

                    batch.setLength(0);
                }

                batch.append(summary)
                        .append("\n\n");
            }

            if (!batch.isEmpty()) {
                nextLevel.add(
                        summarizeIntermediate(
                                batch.toString(),
                                topic,
                                classNumber
                        )
                );
            }

            currentLevel = nextLevel;
        }

        return aiSummaryService.summarize(
                new AiSummaryRequest(
                        currentLevel.get(0),
                        topic,
                        classNumber
                )
        );
    }

    private String summarizeIntermediate(
            String content,
            String topic,
            Integer classNumber) {

        AiSummaryResponse response =
                aiSummaryService.summarize(
                        new AiSummaryRequest(
                                content,
                                topic,
                                classNumber
                        )
                );

        return toSummaryText(response);
    }

    private String toSummaryText(
            AiSummaryResponse summary) {

        StringBuilder builder = new StringBuilder();

        builder.append("Summary:\n")
                .append(summary.summary())
                .append("\n");

        builder.append("Key Points:\n");

        if (summary.keyPoints() != null) {
            summary.keyPoints()
                    .forEach(point ->
                            builder.append("- ")
                                    .append(point)
                                    .append("\n")
                    );
        }

        builder.append("Important Terms:\n");

        if (summary.importantTerms() != null) {
            summary.importantTerms()
                    .forEach(term ->
                            builder.append("- ")
                                    .append(term)
                                    .append("\n")
                    );
        }

        builder.append("Quick Revision Points:\n");

        if (summary.quickRevisionPoints() != null) {
            summary.quickRevisionPoints()
                    .forEach(point ->
                            builder.append("- ")
                                    .append(point)
                                    .append("\n")
                    );
        }
        return builder.toString();
    }
}