package com.smartexam.notification.dto;

import com.smartexam.common.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private Long id;

    private Long userId;

    private Long organizationId;

    private String title;

    private String message;

    private NotificationType type;

    private Boolean read;

    private LocalDateTime createdAt;
}