package com.smartexam.subscription.scheduler;

import com.smartexam.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionExpiryScheduler {

    private final SubscriptionService subscriptionService;

    @Scheduled(fixedDelay = 60000)
    public void expireSubscriptions() {

        log.info("========================================");
        log.info("SUBSCRIPTION EXPIRY SCHEDULER STARTED");
        log.info("========================================");

        subscriptionService.expireSubscriptions();

        log.info("========================================");
        log.info("SUBSCRIPTION EXPIRY SCHEDULER COMPLETED");
        log.info("========================================");
    }
}