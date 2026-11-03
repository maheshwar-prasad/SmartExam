package com.smartexam.subscriptionplan.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.subscriptionplan.dto.SubscriptionPlanRequest;
import com.smartexam.subscriptionplan.dto.SubscriptionPlanResponse;
import com.smartexam.subscriptionplan.entity.SubscriptionPlan;
import com.smartexam.subscriptionplan.repository.SubscriptionPlanRepository;
import com.smartexam.subscriptionplan.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionPlanServiceImpl
        implements SubscriptionPlanService {

    private final SubscriptionPlanRepository repository;


    /**
     * Create subscription plan.
     */
    @Override
    public SubscriptionPlanResponse create(
            SubscriptionPlanRequest request) {

        validateDuplicateName(
                request.name(),
                null
        );

        SubscriptionPlan plan =
                SubscriptionPlan.builder()
                        .name(request.name().trim())
                        .description(request.description())
                        .price(request.price())
                        .durationDays(request.durationDays())
                        .active(true)
                        .build();

        SubscriptionPlan saved =
                repository.save(plan);

        return mapToResponse(saved);
    }


    /**
     * Get subscription plan by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanResponse getById(
            Long id) {

        SubscriptionPlan plan =
                repository.findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subscription plan not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(plan);
    }


    /**
     * Get all active subscription plans.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> getAll() {

        return repository
                .findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    /**
     * Get paginated active subscription plans.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionPlanResponse> getPage(
            Pageable pageable) {

        return repository
                .findByActiveTrue(pageable)
                .map(this::mapToResponse);
    }


    /**
     * Update subscription plan.
     */
    @Override
    public SubscriptionPlanResponse update(
            Long id,
            SubscriptionPlanRequest request) {

        SubscriptionPlan plan =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subscription plan not found with id: "
                                                + id
                                )
                        );

        validateDuplicateName(
                request.name(),
                id
        );

        plan.setName(
                request.name().trim()
        );

        plan.setDescription(
                request.description()
        );

        plan.setPrice(
                request.price()
        );

        plan.setDurationDays(
                request.durationDays()
        );

        SubscriptionPlan updated =
                repository.save(plan);

        return mapToResponse(updated);
    }


    /**
     * Soft delete subscription plan.
     */
    @Override
    public void delete(Long id) {

        SubscriptionPlan plan =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subscription plan not found with id: "
                                                + id
                                )
                        );

        plan.setActive(false);

        repository.save(plan);
    }


    /**
     * Activate subscription plan.
     */
    @Override
    public SubscriptionPlanResponse activate(
            Long id) {

        SubscriptionPlan plan =
                findById(id);

        plan.setActive(true);

        return mapToResponse(
                repository.save(plan)
        );
    }


    /**
     * Deactivate subscription plan.
     */
    @Override
    public SubscriptionPlanResponse deactivate(
            Long id) {

        SubscriptionPlan plan =
                findById(id);

        plan.setActive(false);

        return mapToResponse(
                repository.save(plan)
        );
    }


    private SubscriptionPlan findById(
            Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription plan not found with id: "
                                        + id
                        )
                );
    }


    private void validateDuplicateName(
            String name,
            Long id) {

        String normalizedName =
                name.trim();

        boolean exists;

        if (id == null) {

            exists =
                    repository.existsByNameIgnoreCase(
                            normalizedName
                    );

        } else {

            exists =
                    repository
                            .existsByNameIgnoreCaseAndIdNot(
                                    normalizedName,
                                    id
                            );
        }

        if (exists) {

            throw new IllegalArgumentException(
                    "Subscription plan already exists with name: "
                            + normalizedName
            );
        }
    }


    private SubscriptionPlanResponse mapToResponse(
            SubscriptionPlan plan) {

        return new SubscriptionPlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                plan.getPrice(),
                plan.getDurationDays(),
                plan.getActive(),
                plan.getCreatedAt(),
                plan.getUpdatedAt()
        );
    }
}