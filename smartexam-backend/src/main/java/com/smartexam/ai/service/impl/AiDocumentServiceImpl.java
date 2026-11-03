package com.smartexam.ai.service.impl;

import com.smartexam.ai.dto.AiDocumentResponse;
import com.smartexam.ai.service.AiDocumentService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class AiDocumentServiceImpl
        implements AiDocumentService {

    private static final long MAX_FILE_SIZE =
            20 * 1024 * 1024;

    @Override
    public AiDocumentResponse extractText(
            MultipartFile file) {

        validateFile(file);

        try {

            Resource resource =
                    new InputStreamResource(
                            file.getInputStream()
                    ) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };

            TikaDocumentReader reader =
                    new TikaDocumentReader(resource);

            List<Document> documents =
                    reader.read();

            String extractedText =
                    documents.stream()
                            .map(Document::getText)
                            .filter(text ->
                                    text != null &&
                                            !text.isBlank()
                            )
                            .reduce(
                                    "",
                                    (a, b) ->
                                            a.isBlank()
                                                    ? b
                                                    : a + "\n\n" + b
                            );

            if (extractedText.isBlank()) {

                throw new IllegalStateException(
                        "No text could be extracted from the document"
                );
            }

            log.info(
                    "Document text extracted. file={}, documents={}, characters={}",
                    file.getOriginalFilename(),
                    documents.size(),
                    extractedText.length()
            );

            return new AiDocumentResponse(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    documents.size(),
                    extractedText.length(),
                    extractedText
            );

        } catch (IOException e) {

            log.error(
                    "Failed to read document: {}",
                    file.getOriginalFilename(),
                    e
            );

            throw new IllegalStateException(
                    "Failed to read uploaded document",
                    e
            );
        }
    }

    private void validateFile(
            MultipartFile file) {

        if (file == null ||
                file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Document file is required"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Maximum document size is 20 MB"
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null ||
                fileName.isBlank()) {

            throw new IllegalArgumentException(
                    "Document filename is required"
            );
        }

        String lowerCase =
                fileName.toLowerCase();

        boolean supported =
                lowerCase.endsWith(".pdf")
                        || lowerCase.endsWith(".doc")
                        || lowerCase.endsWith(".docx")
                        || lowerCase.endsWith(".ppt")
                        || lowerCase.endsWith(".pptx")
                        || lowerCase.endsWith(".txt")
                        || lowerCase.endsWith(".html")
                        || lowerCase.endsWith(".htm");

        if (!supported) {

            throw new IllegalArgumentException(
                    "Unsupported document format"
            );
        }
    }
}