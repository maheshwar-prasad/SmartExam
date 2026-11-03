package com.smartexam.ai.document.service.impl;

import com.smartexam.ai.document.config.AiDocumentStorageProperties;
import com.smartexam.ai.document.service.AiDocumentStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalAiDocumentStorageService
        implements AiDocumentStorageService {

    private final AiDocumentStorageProperties properties;

    @Override
    public String store(
            Long organizationId,
            Long documentId,
            MultipartFile file) {

        try {

            Path basePath =
                    Paths.get(properties.getBasePath())
                            .toAbsolutePath()
                            .normalize();

            Path organizationPath =
                    basePath.resolve(
                            String.valueOf(organizationId)
                    );

            Path documentPath =
                    organizationPath.resolve(
                            String.valueOf(documentId)
                    );

            Files.createDirectories(documentPath);

            String fileName =
                    sanitizeFileName(
                            file.getOriginalFilename()
                    );

            Path target =
                    documentPath.resolve(fileName)
                            .normalize();

            if (!target.startsWith(documentPath)) {
                throw new IllegalArgumentException(
                        "Invalid file name"
                );
            }

            try (InputStream inputStream =
                         file.getInputStream()) {

                Files.copy(
                        inputStream,
                        target
                );
            }

            String storageKey =
                    organizationId
                            + "/"
                            + documentId
                            + "/"
                            + fileName;

            log.info(
                    "AI document stored. documentId={}, storageKey={}",
                    documentId,
                    storageKey
            );

            return storageKey;

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Failed to store AI document",
                    exception
            );
        }
    }

    @Override
    public Resource load(String storageKey) {

        Path basePath =
                Paths.get(properties.getBasePath())
                        .toAbsolutePath()
                        .normalize();

        Path filePath =
                basePath.resolve(storageKey)
                        .normalize();

        if (!filePath.startsWith(basePath)) {

            throw new IllegalArgumentException(
                    "Invalid storage key"
            );
        }

        FileSystemResource resource =
                new FileSystemResource(filePath);

        if (!resource.exists() ||
                !resource.isReadable()) {

            throw new IllegalArgumentException(
                    "Stored document not found: "
                            + storageKey
            );
        }

        return resource;
    }

    @Override
    public void delete(String storageKey) {

        if (storageKey == null ||
                storageKey.isBlank()) {
            return;
        }

        try {

            Path basePath =
                    Paths.get(properties.getBasePath())
                            .toAbsolutePath()
                            .normalize();

            Path filePath =
                    basePath.resolve(storageKey)
                            .normalize();

            if (!filePath.startsWith(basePath)) {

                throw new IllegalArgumentException(
                        "Invalid storage key"
                );
            }

            Files.deleteIfExists(filePath);

            log.info(
                    "AI document file deleted. storageKey={}",
                    storageKey
            );

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Failed to delete stored AI document",
                    exception
            );
        }
    }

    @Override
    public boolean exists(String storageKey) {

        if (storageKey == null ||
                storageKey.isBlank()) {

            return false;
        }

        Path basePath =
                Paths.get(properties.getBasePath())
                        .toAbsolutePath()
                        .normalize();

        Path filePath =
                basePath.resolve(storageKey)
                        .normalize();

        return filePath.startsWith(basePath)
                && Files.exists(filePath);
    }

    private String sanitizeFileName(
            String fileName) {

        if (fileName == null ||
                fileName.isBlank()) {

            throw new IllegalArgumentException(
                    "File name is required"
            );
        }

        String sanitized =
                Paths.get(fileName)
                        .getFileName()
                        .toString();

        sanitized =
                sanitized.replaceAll(
                        "[^a-zA-Z0-9._-]",
                        "_"
                );

        if (sanitized.isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid file name"
            );
        }

        return sanitized;
    }
}