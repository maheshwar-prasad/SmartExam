package com.smartexam.ai.service;

import com.smartexam.ai.dto.AiSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AiDocumentSummaryService {

    AiSummaryResponse summarize(
            MultipartFile file,
            String topic,
            Integer classNumber
    );
}