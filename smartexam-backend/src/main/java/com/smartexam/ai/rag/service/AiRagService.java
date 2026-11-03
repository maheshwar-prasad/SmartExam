package com.smartexam.ai.rag.service;

import com.smartexam.ai.rag.dto.AiRagChatResponse;
import com.smartexam.ai.rag.dto.AiRagIngestResponse;
import com.smartexam.ai.rag.dto.AiRagReindexResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AiRagService {

    AiRagIngestResponse ingest(
            MultipartFile file,
            Long subjectId,
            Integer classNumber
    );

    AiRagChatResponse chat(
            String question,
            String topic,
            Long subjectId,
            Integer classNumber
    );

    AiRagReindexResponse reindex(Long documentId);
}