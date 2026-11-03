package com.smartexam.notification.dto;

import com.smartexam.common.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {

    private Long userId;

    private Long organizationId;

    private String title;

    private String message;

    private NotificationType type;
}