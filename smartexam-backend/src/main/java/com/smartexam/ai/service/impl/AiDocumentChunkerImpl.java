package com.smartexam.ai.service.impl;

import com.smartexam.ai.service.AiDocumentChunker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiDocumentChunkerImpl implements AiDocumentChunker {

    @Override
    public List<String> chunk(
            String text,
            int chunkSize,
            int overlap) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be greater than zero"
            );
        }

        if (overlap < 0 || overlap >= chunkSize) {
            throw new IllegalArgumentException(
                    "Overlap must be between 0 and chunk size"
            );
        }

        String normalizedText = normalize(text);

        List<String> chunks = new ArrayList<>();

        int start = 0;
        int textLength = normalizedText.length();

        while (start < textLength) {

            int end = Math.min(
                    start + chunkSize,
                    textLength
            );

            /*
             * Try to end the chunk at a natural boundary.
             */
            if (end < textLength) {

                int paragraphBreak =
                        normalizedText.lastIndexOf("\n\n", end);

                int sentenceBreak =
                        normalizedText.lastIndexOf(". ", end);

                if (paragraphBreak > start + (chunkSize / 2)) {
                    end = paragraphBreak + 2;
                } else if (sentenceBreak > start + (chunkSize / 2)) {
                    end = sentenceBreak + 2;
                }
            }

            String chunk =
                    normalizedText.substring(start, end).trim();

            if (!chunk.isBlank()) {
                chunks.add(chunk);
            }

            if (end >= textLength) {
                break;
            }

            start = Math.max(
                    end - overlap,
                    start + 1
            );
        }

        return chunks;
    }

    private String normalize(String text) {

        return text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replaceAll("[ \t]+", " ")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }
}