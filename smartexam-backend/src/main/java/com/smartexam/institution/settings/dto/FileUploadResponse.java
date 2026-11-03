package com.smartexam.institution.settings.dto;

public record FileUploadResponse(
        String fileUrl,
        String fileName,
        String originalFileName,
        String contentType,
        long size
) {
}