package com.smartexam.ai.service;

import java.util.List;

public interface AiDocumentChunker {

    List<String> chunk(
            String text,
            int chunkSize,
            int overlap
    );
}