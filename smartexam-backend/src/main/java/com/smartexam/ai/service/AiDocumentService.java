package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiDocumentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AiDocumentService {

    AiDocumentResponse extractText(
            MultipartFile file
    );
}