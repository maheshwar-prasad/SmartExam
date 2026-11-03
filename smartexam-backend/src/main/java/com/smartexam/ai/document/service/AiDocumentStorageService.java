package com.smartexam.ai.document.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AiDocumentStorageService {

    String store(
            Long organizationId,
            Long documentId,
            MultipartFile file
    );

    Resource load(String storageKey);

    void delete(String storageKey);

    boolean exists(String storageKey);
}