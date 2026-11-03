package com.smartexam.ai.service;

import java.util.Map;

public interface AiPromptBuilder {

    String build(
            String instruction,
            Map<String, Object> variables
    );
}