package com.smartexam.ai.document.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "smartexam.ai.document-storage")
public class AiDocumentStorageProperties {

    private String type = "local";

    private String basePath = "./storage/ai-documents";
}