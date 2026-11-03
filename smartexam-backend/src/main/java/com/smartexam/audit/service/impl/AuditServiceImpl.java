package com.smartexam.audit.service.impl;

import com.smartexam.audit.dto.AuditLogResponse;
import com.smartexam.audit.entity.AuditAction;
import com.smartexam.audit.entity.AuditLog;
import com.smartexam.audit.repository.AuditLogRepository;
import com.smartexam.audit.service.AuditService;
import com.smartexam.security.service.CurrentUserService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl
        implements AuditService {

    private final AuditLogRepository auditLogRepository;

    private final CurrentUserService currentUserService;

    private final HttpServletRequest httpServletRequest;


    @Override
    @Transactional(propagation =
            org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void log(
            AuditAction action,
            String entityType,
            String entityId,
            String description,
            boolean success) {

        log(
                action,
                entityType,
                entityId,
                description,
                success,
                null
        );
    }


    @Override
    @Transactional(propagation =
            org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void log(
            AuditAction action,
            String entityType,
            String entityId,
            String description,
            boolean success,
            String metadata) {

        try {

            Long organizationId =
                    currentUserService.getOrganizationId();

            Long userId =
                    getCurrentUserId();

            AuditLog auditLog =
                    AuditLog.builder()

                            .organizationId(
                                    organizationId
                            )

                            .userId(
                                    userId
                            )

                            .action(
                                    action
                            )

                            .entityType(
                                    entityType
                            )

                            .entityId(
                                    entityId
                            )

                            .description(
                                    description
                            )

                            .ipAddress(
                                    getClientIp()
                            )

                            .userAgent(
                                    getUserAgent()
                            )

                            .requestMethod(
                                    httpServletRequest
                                            .getMethod()
                            )

                            .requestUri(
                                    httpServletRequest
                                            .getRequestURI()
                            )

                            .success(
                                    success
                            )

                            .metadata(
                                    metadata
                            )

                            .build();

            auditLogRepository.save(
                    auditLog
            );

        } catch (Exception exception) {

            /*
             * Audit must not break the
             * business operation.
             */

            log.error(
                    "Failed to create audit log. action={}",
                    action,
                    exception
            );
        }
    }


    @Override
    @Transactional(propagation =
            org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void logFailure(
            AuditAction action,
            String entityType,
            String entityId,
            String description,
            Exception exception) {

        try {

            Long organizationId =
                    currentUserService.getOrganizationId();

            AuditLog auditLog =
                    AuditLog.builder()

                            .organizationId(
                                    organizationId
                            )

                            .userId(
                                    getCurrentUserId()
                            )

                            .action(
                                    action
                            )

                            .entityType(
                                    entityType
                            )

                            .entityId(
                                    entityId
                            )

                            .description(
                                    description
                            )

                            .ipAddress(
                                    getClientIp()
                            )

                            .userAgent(
                                    getUserAgent()
                            )

                            .requestMethod(
                                    httpServletRequest
                                            .getMethod()
                            )

                            .requestUri(
                                    httpServletRequest
                                            .getRequestURI()
                            )

                            .success(false)

                            .errorMessage(
                                    safeMessage(
                                            exception
                                    )
                            )

                            .build();

            auditLogRepository.save(
                    auditLog
            );

        } catch (Exception auditException) {

            log.error(
                    "Failed to create failure audit log",
                    auditException
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogs() {

        Long organizationId =
                currentUserService.getOrganizationId();

        return auditLogRepository
                .findAll()
                .stream()

                .filter(log ->
                        organizationId.equals(
                                log.getOrganizationId()
                        )
                )

                .map(this::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public AuditLogResponse getAuditLog(
            Long id) {

        Long organizationId =
                currentUserService.getOrganizationId();

        AuditLog auditLog =
                auditLogRepository
                        .findById(id)
                        .filter(log ->
                                organizationId.equals(
                                        log.getOrganizationId()
                                )
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Audit log not found"
                                )
                        );

        return toResponse(
                auditLog
        );
    }


    private AuditLogResponse toResponse(
            AuditLog auditLog) {

        return new AuditLogResponse(

                auditLog.getId(),

                auditLog.getOrganizationId(),

                auditLog.getUserId(),

                auditLog.getAction(),

                auditLog.getEntityType(),

                auditLog.getEntityId(),

                auditLog.getDescription(),

                auditLog.getIpAddress(),

                auditLog.getRequestMethod(),

                auditLog.getRequestUri(),

                auditLog.getSuccess(),

                auditLog.getErrorMessage(),

                auditLog.getMetadata(),

                auditLog.getCreatedAt()
        );
    }


    private Long getCurrentUserId() {

        /*
         * Adjust this method to your existing
         * CurrentUserService API if the method
         * is named differently.
         */

        return currentUserService.getUserId();
    }


    private String getClientIp() {

        String forwarded =
                httpServletRequest.getHeader(
                        "X-Forwarded-For"
                );

        if (forwarded != null &&
                !forwarded.isBlank()) {

            return forwarded
                    .split(",")[0]
                    .trim();
        }

        return httpServletRequest
                .getRemoteAddr();
    }


    private String getUserAgent() {

        String userAgent =
                httpServletRequest.getHeader(
                        "User-Agent"
                );

        if (userAgent == null) {
            return null;
        }

        return userAgent.length() > 1000
                ? userAgent.substring(0, 1000)
                : userAgent;
    }


    private String safeMessage(
            Exception exception) {

        if (exception == null) {
            return null;
        }

        String message =
                exception.getMessage();

        if (message == null ||
                message.isBlank()) {

            return exception
                    .getClass()
                    .getSimpleName();
        }

        return message.length() > 2000
                ? message.substring(0, 2000)
                : message;
    }
}