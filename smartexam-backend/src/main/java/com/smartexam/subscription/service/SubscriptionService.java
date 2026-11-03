package com.smartexam.subscription.service;

import com.smartexam.subscription.dto.SubscriptionRequest;
import com.smartexam.subscription.dto.SubscriptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {

    SubscriptionResponse create(SubscriptionRequest request);

    SubscriptionResponse getById(Long id);

    List<SubscriptionResponse> getMySubscriptions();

    Page<SubscriptionResponse> getOrganizationSubscriptions(Pageable pageable);

    SubscriptionResponse activate(Long id);

    SubscriptionResponse cancel(Long id);

    SubscriptionResponse expire(Long id);

    void expireSubscriptions();
}