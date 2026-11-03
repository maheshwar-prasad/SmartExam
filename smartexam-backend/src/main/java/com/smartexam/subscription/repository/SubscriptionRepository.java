package com.smartexam.subscription.repository;

import com.smartexam.common.enums.SubscriptionStatus;
import com.smartexam.subscription.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long>,
        JpaSpecificationExecutor<Subscription> {

    Optional<Subscription> findByIdAndActiveTrue(
            Long id
    );

    List<Subscription>
    findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    Page<Subscription>
    findByOrganizationIdAndActiveTrue(
            Long organizationId,
            Pageable pageable
    );

    List<Subscription>
    findByStatusAndActiveTrue(
            SubscriptionStatus status
    );

    List<Subscription>
    findByEndDateBeforeAndStatusAndActiveTrue(
            LocalDate date,
            SubscriptionStatus status
    );

    long countByStatusAndActiveTrue(
            SubscriptionStatus status
    );

    long countByActiveTrue();
}