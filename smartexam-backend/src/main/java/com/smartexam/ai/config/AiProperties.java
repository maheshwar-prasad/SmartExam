package com.smartexam.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "smartexam.ai")
public class AiProperties {

    private int maxRetry = 3;

    private int maxQuestionsPerRequest = 50;

    private int maxPromptLength = 10000;

    private boolean enabled = true;

    private int documentChunkSize = 7000;

    private int documentChunkOverlap = 300;

    private int documentMaxChunks = 50;
}