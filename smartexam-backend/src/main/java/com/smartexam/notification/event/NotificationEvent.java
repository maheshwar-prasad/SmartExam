package com.smartexam.notification.event;

import com.smartexam.common.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    private Long userId;

    private Long organizationId;

    private String title;

    private String message;

    private NotificationType type;
}