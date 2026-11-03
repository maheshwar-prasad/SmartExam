package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiQuestionGenerationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AiDocumentQuestionGenerationService {

    AiQuestionGenerationResponse generate(
            MultipartFile file,
            Integer classNumber,
            Long subjectId,
            String topic,
            String difficulty,
            Integer count,
            Boolean save
    );
}