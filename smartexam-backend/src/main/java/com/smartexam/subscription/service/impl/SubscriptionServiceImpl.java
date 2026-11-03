package com.smartexam.subscription.service.impl;

import com.smartexam.common.enums.NotificationType;
import com.smartexam.common.enums.SubscriptionStatus;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.notification.event.NotificationEvent;
import com.smartexam.organization.entity.Organization;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.subscription.dto.SubscriptionRequest;
import com.smartexam.subscription.dto.SubscriptionResponse;
import com.smartexam.subscription.entity.Subscription;
import com.smartexam.subscription.event.SubscriptionNotificationPublisher;
import com.smartexam.subscription.repository.SubscriptionRepository;
import com.smartexam.subscription.service.SubscriptionService;
import com.smartexam.subscriptionplan.entity.SubscriptionPlan;
import com.smartexam.subscriptionplan.repository.SubscriptionPlanRepository;
import com.smartexam.user.entity.User;
import com.smartexam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final UserRepository userRepository;

    private final CurrentUserService currentUserService;

    private final SubscriptionNotificationPublisher subscriptionNotificationPublisher;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public SubscriptionResponse create(SubscriptionRequest request) {

        Long userId = currentUserService.getUserId();

        Long organizationId = currentUserService.getOrganizationId();

        log.info("Creating subscription for userId={}, organizationId={}, planId={}", userId, organizationId, request.getPlanId());

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Current user not found: " + userId));

        SubscriptionPlan plan = subscriptionPlanRepository.findByIdAndActiveTrue(request.getPlanId()).orElseThrow(() -> new ResourceNotFoundException("Subscription plan not found: " + request.getPlanId()));

        /*
         * Additional safety check.
         */
        if (!user.getOrganization().getId().equals(organizationId)) {

            throw new AccessDeniedException("User does not belong to the requested organization");
        }

        /*
         * Prevent multiple active subscriptions.
         */
        boolean activeSubscriptionExists = subscriptionRepository.findByOrganizationIdAndActiveTrue(organizationId).stream().anyMatch(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE);

        if (activeSubscriptionExists) {

            throw new IllegalStateException("Organization already has an active subscription.");
        }

        LocalDate startDate = LocalDate.now();

        LocalDate endDate = startDate.plusDays(plan.getDurationDays());

        Organization organization = user.getOrganization();

        Subscription subscription = Subscription.builder()
                .organization(organization)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .status(SubscriptionStatus.PENDING)
                .amount(plan.getPrice())
                .active(true)
                .autoRenew(false)
                .createdByUser(user)
                .remarks("Subscription created and awaiting payment")
                .build();

        Subscription saved = subscriptionRepository.save(subscription);

        log.info("Subscription created successfully. subscriptionId={}", saved.getId());

        return mapToResponse(saved);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SubscriptionResponse getById(Long id) {

        Long organizationId = currentUserService.getOrganizationId();

        Subscription subscription = subscriptionRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + id));

        validateOrganizationAccess(subscription, organizationId);

        return mapToResponse(subscription);
    }


    // =========================================================
    // MY SUBSCRIPTIONS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getMySubscriptions() {

        Long organizationId = currentUserService.getOrganizationId();

        return subscriptionRepository.findByOrganizationIdAndActiveTrue(organizationId).stream().map(this::mapToResponse).toList();
    }


    // =========================================================
    // ORGANIZATION SUBSCRIPTIONS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionResponse> getOrganizationSubscriptions(Pageable pageable) {

        Long organizationId = currentUserService.getOrganizationId();

        return subscriptionRepository.findByOrganizationIdAndActiveTrue(organizationId, pageable).map(this::mapToResponse);
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public SubscriptionResponse activate(Long id) {

        Long organizationId = currentUserService.getOrganizationId();

        Subscription subscription = getSubscriptionForOrganization(id, organizationId);

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {

            throw new IllegalStateException("Cancelled subscription cannot be activated.");
        }

        subscription.setStatus(SubscriptionStatus.ACTIVE);

        subscription.setActive(true);

        Subscription saved = subscriptionRepository.save(subscription);

        return mapToResponse(saved);
    }


    // =========================================================
    // CANCEL
    // =========================================================

    @Override
    public SubscriptionResponse cancel(Long id) {

        Long organizationId = currentUserService.getOrganizationId();

        Subscription subscription = getSubscriptionForOrganization(id, organizationId);

        if (subscription.getStatus() == SubscriptionStatus.EXPIRED) {

            throw new IllegalStateException("Expired subscription cannot be cancelled.");
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);

        subscription.setActive(false);

        Subscription saved = subscriptionRepository.save(subscription);

        /*
         * Optional notification.
         */
        if (subscription.getCreatedByUser() != null) {

            subscriptionNotificationPublisher.publishCancelled(subscription.getCreatedByUser().getId(),

                    organizationId);
        }

        return mapToResponse(saved);
    }


    // =========================================================
    // EXPIRE
    // =========================================================

    @Override
    public SubscriptionResponse expire(Long id) {

        Long organizationId = currentUserService.getOrganizationId();

        Subscription subscription = getSubscriptionForOrganization(id, organizationId);

        subscription.setStatus(SubscriptionStatus.EXPIRED);

        subscription.setActive(false);

        Subscription saved = subscriptionRepository.save(subscription);

        return mapToResponse(saved);
    }


    // =========================================================
    // SCHEDULED EXPIRY
    // =========================================================

    @Override
    public void expireSubscriptions() {

        LocalDate today = LocalDate.now();

        log.info("Checking expired subscriptions. date={}", today);

        List<Subscription> subscriptions = subscriptionRepository.findByEndDateBeforeAndStatusAndActiveTrue(today, SubscriptionStatus.ACTIVE);

        log.info("Found {} subscriptions to expire.", subscriptions.size());

        for (Subscription subscription : subscriptions) {

            log.info("Expiring subscription id={}", subscription.getId());

            subscription.setStatus(SubscriptionStatus.EXPIRED);

            subscription.setActive(false);

            if (subscription.getCreatedByUser() != null) {

                subscriptionNotificationPublisher.publishExpired(subscription.getCreatedByUser().getId(),

                        subscription.getOrganization().getId());
            }
        }

        if (!subscriptions.isEmpty()) {

            subscriptionRepository.saveAll(subscriptions);
        }
    }


    // =========================================================
    // HELPERS
    // =========================================================

    private Subscription getSubscriptionForOrganization(Long id, Long organizationId) {

        Subscription subscription = subscriptionRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + id));

        validateOrganizationAccess(subscription, organizationId);

        return subscription;
    }


    private void validateOrganizationAccess(Subscription subscription, Long organizationId) {

        if (!subscription.getOrganization().getId().equals(organizationId)) {

            throw new AccessDeniedException("You do not have access to this subscription.");
        }
    }


    private SubscriptionResponse mapToResponse(Subscription subscription) {

        return SubscriptionResponse.builder().id(subscription.getId()).organizationId(subscription.getOrganization().getId()).userId(subscription.getCreatedByUser() != null ? subscription.getCreatedByUser().getId() : null).planId(subscription.getPlan().getId()).planName(subscription.getPlan().getName()).amount(subscription.getPlan().getPrice()).status(subscription.getStatus().name()).startDate(subscription.getStartDate()).endDate(subscription.getEndDate()).active(subscription.getActive()).autoRenew(subscription.getAutoRenew()).remarks(subscription.getRemarks()).build();
    }
}