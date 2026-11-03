package com.smartexam.notification.service;

import com.smartexam.notification.dto.CreateNotificationRequest;
import com.smartexam.notification.dto.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    NotificationResponse create(
            CreateNotificationRequest request
    );

    Page<NotificationResponse> getMyNotifications(
            Pageable pageable
    );

    Page<NotificationResponse> getMyUnreadNotifications(
            Pageable pageable
    );

    long getUnreadCount();

    void markAsRead(Long notificationId);

    void markAllAsRead();
}