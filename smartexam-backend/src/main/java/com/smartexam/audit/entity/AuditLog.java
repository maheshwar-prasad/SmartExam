package com.smartexam.audit.entity;

import com.smartexam.common.entity.BaseEntity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "audit_logs",
        indexes = {

                @Index(
                        name = "idx_audit_organization",
                        columnList = "organization_id"
                ),

                @Index(
                        name = "idx_audit_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_audit_action",
                        columnList = "action"
                ),

                @Index(
                        name = "idx_audit_entity",
                        columnList = "entity_type,entity_id"
                ),

                @Index(
                        name = "idx_audit_created_at",
                        columnList = "created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AuditLog extends BaseEntity {

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Column(
            name = "user_id"
    )
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "action",
            nullable = false,
            length = 80
    )
    private AuditAction action;

    @Column(
            name = "entity_type",
            length = 100
    )
    private String entityType;

    @Column(
            name = "entity_id",
            length = 100
    )
    private String entityId;

    @Column(
            name = "description",
            length = 1000
    )
    private String description;

    @Column(
            name = "ip_address",
            length = 100
    )
    private String ipAddress;

    @Column(
            name = "user_agent",
            length = 1000
    )
    private String userAgent;

    @Column(
            name = "request_method",
            length = 20
    )
    private String requestMethod;

    @Column(
            name = "request_uri",
            length = 1000
    )
    private String requestUri;

    @Column(
            name = "success",
            nullable = false
    )
    private Boolean success;

    @Column(
            name = "error_message",
            columnDefinition = "TEXT"
    )
    private String errorMessage;

    @Column(
            name = "metadata",
            columnDefinition = "JSON"
    )
    private String metadata;
}