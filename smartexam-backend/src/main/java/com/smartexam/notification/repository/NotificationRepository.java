package com.smartexam.notification.repository;

import com.smartexam.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdAndActiveTrueOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );

    Page<Notification> findByUserIdAndReadFalseAndActiveTrueOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );

    long countByUserIdAndReadFalseAndActiveTrue(
            Long userId
    );

    long countByOrganizationIdAndReadFalseAndActiveTrue(
            Long organizationId
    );
}