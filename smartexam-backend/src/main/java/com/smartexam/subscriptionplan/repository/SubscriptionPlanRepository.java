package com.smartexam.subscriptionplan.repository;

import com.smartexam.subscriptionplan.entity.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, Long>,
        JpaSpecificationExecutor<SubscriptionPlan> {

    /**
     * Find an active subscription plan by ID.
     */
    Optional<SubscriptionPlan> findByIdAndActiveTrue(
            Long id
    );

    /**
     * Find subscription plan by name.
     */
    Optional<SubscriptionPlan> findByNameIgnoreCase(
            String name
    );

    /**
     * Get all active subscription plans.
     */
    List<SubscriptionPlan> findByActiveTrue();

    /**
     * Get active subscription plans with pagination.
     */
    Page<SubscriptionPlan> findByActiveTrue(
            Pageable pageable
    );

    /**
     * Check duplicate plan name during create.
     */
    boolean existsByNameIgnoreCase(
            String name
    );

    /**
     * Check duplicate plan name during update.
     */
    boolean existsByNameIgnoreCaseAndIdNot(
            String name,
            Long id
    );
}