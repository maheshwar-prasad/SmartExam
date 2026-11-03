package com.smartexam.subscriptionplan.service;

import com.smartexam.subscriptionplan.dto.SubscriptionPlanRequest;
import com.smartexam.subscriptionplan.dto.SubscriptionPlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionPlanService {

    SubscriptionPlanResponse create(
            SubscriptionPlanRequest request
    );

    SubscriptionPlanResponse getById(
            Long id
    );

    List<SubscriptionPlanResponse> getAll();

    Page<SubscriptionPlanResponse> getPage(
            Pageable pageable
    );

    SubscriptionPlanResponse update(
            Long id,
            SubscriptionPlanRequest request
    );

    void delete(
            Long id
    );

    SubscriptionPlanResponse activate(
            Long id
    );

    SubscriptionPlanResponse deactivate(
            Long id
    );
}