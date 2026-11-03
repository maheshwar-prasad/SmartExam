package com.smartexam.audit.service;

import com.smartexam.audit.dto.AuditLogResponse;
import com.smartexam.audit.entity.AuditAction;

import java.util.List;

public interface AuditService {

    void log(
            AuditAction action,
            String entityType,
            String entityId,
            String description,
            boolean success
    );

    void log(
            AuditAction action,
            String entityType,
            String entityId,
            String description,
            boolean success,
            String metadata
    );

    void logFailure(
            AuditAction action,
            String entityType,
            String entityId,
            String description,
            Exception exception
    );

    List<AuditLogResponse> getAuditLogs();

    AuditLogResponse getAuditLog(Long id);
}