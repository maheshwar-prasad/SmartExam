package com.smartexam.notification.service.impl;

import com.smartexam.notification.dto.CreateNotificationRequest;
import com.smartexam.notification.dto.NotificationResponse;
import com.smartexam.notification.entity.Notification;
import com.smartexam.notification.repository.NotificationRepository;
import com.smartexam.notification.service.NotificationService;
import com.smartexam.notification.sse.NotificationSseService;
import com.smartexam.user.entity.User;
import com.smartexam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;
    private final NotificationSseService notificationSseService;

    private static final Logger log =
            LoggerFactory.getLogger(NotificationServiceImpl.class);
    // =========================================================
    // CREATE NOTIFICATION
    // =========================================================

    @Override
    public NotificationResponse create(
            CreateNotificationRequest request) {

        User targetUser =
                userRepository.findById(request.getUserId())
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found: "
                                                + request.getUserId()
                                )
                        );

        /*
         * Never blindly trust organizationId from request.
         *
         * The notification organization should match the
         * target user's organization.
         */
        Long targetOrganizationId =
                targetUser.getOrganization().getId();

        if (request.getOrganizationId() != null
                && !targetOrganizationId.equals(
                request.getOrganizationId())) {

            throw new IllegalArgumentException(
                    "Notification organization does not match user organization"
            );
        }

        Notification notification =
                Notification.builder()
                        .organizationId(targetOrganizationId)
                        .user(targetUser)
                        .title(request.getTitle())
                        .message(request.getMessage())
                        .type(request.getType())
                        .read(false)
                        .active(true)
                        .build();

        Notification saved =
                notificationRepository.save(notification);

        NotificationResponse response =
                mapToResponse(saved);

        notificationSseService.sendToUser(
                targetUser.getId(),
                response
        );

        return response;
    }


    // =========================================================
    // MY NOTIFICATIONS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(
            Pageable pageable) {

        User currentUser = getCurrentUser();

        log.info(
                "Fetching notifications for userId={}, email={}",
                currentUser.getId(),
                currentUser.getEmail()
        );

        Page<Notification> notifications =
                notificationRepository
                        .findByUserIdAndActiveTrueOrderByCreatedAtDesc(
                                currentUser.getId(),
                                pageable
                        );

        log.info(
                "Notification count for userId={}: {}",
                currentUser.getId(),
                notifications.getTotalElements()
        );

        return notifications.map(this::mapToResponse);
    }


    // =========================================================
    // MY UNREAD NOTIFICATIONS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse>
    getMyUnreadNotifications(
            Pageable pageable) {

        User currentUser = getCurrentUser();

        return notificationRepository
                .findByUserIdAndReadFalseAndActiveTrueOrderByCreatedAtDesc(
                        currentUser.getId(),
                        pageable
                )
                .map(this::mapToResponse);
    }


    // =========================================================
    // UNREAD COUNT
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount() {

        User currentUser = getCurrentUser();

        return notificationRepository
                .countByUserIdAndReadFalseAndActiveTrue(
                        currentUser.getId()
                );
    }


    // =========================================================
    // MARK ONE AS READ
    // =========================================================

    @Override
    public void markAsRead(Long notificationId) {

        User currentUser = getCurrentUser();

        Notification notification =
                notificationRepository.findById(
                                notificationId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification not found: "
                                                + notificationId
                                )
                        );

        /*
         * Security:
         * A user can only modify their own notification.
         */
        if (!notification.getUser()
                .getId()
                .equals(currentUser.getId())) {

            throw new AccessDeniedException(
                    "You cannot modify this notification"
            );
        }

        if (!Boolean.TRUE.equals(
                notification.getActive())) {

            return;
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }


    // =========================================================
    // MARK ALL AS READ
    // =========================================================

    @Override
    public void markAllAsRead() {

        User currentUser = getCurrentUser();

        Page<Notification> notifications =
                notificationRepository
                        .findByUserIdAndReadFalseAndActiveTrueOrderByCreatedAtDesc(
                                currentUser.getId(),
                                Pageable.unpaged()
                        );

        notifications.forEach(notification ->
                notification.setRead(true)
        );

        notificationRepository.saveAll(
                notifications.getContent()
        );
    }


    // =========================================================
    // CURRENT USER
    // =========================================================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            throw new AccessDeniedException(
                    "User is not authenticated"
            );
        }

        String email =
                authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + email
                        )
                );
    }


    // =========================================================
    // ENTITY → RESPONSE
    // =========================================================

    private NotificationResponse mapToResponse(
            Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(
                        notification.getUser().getId()
                )
                .organizationId(
                        notification.getOrganizationId()
                )
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}