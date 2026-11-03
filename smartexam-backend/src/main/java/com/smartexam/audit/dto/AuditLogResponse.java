package com.smartexam.audit.dto;

import com.smartexam.audit.entity.AuditAction;

import java.time.LocalDateTime;

public record AuditLogResponse(

        Long id,

        Long organizationId,

        Long userId,

        AuditAction action,

        String entityType,

        String entityId,

        String description,

        String ipAddress,

        String requestMethod,

        String requestUri,

        Boolean success,

        String errorMessage,

        String metadata,

        LocalDateTime createdAt

) {
}