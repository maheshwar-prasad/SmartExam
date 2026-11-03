package com.smartexam.subscriptionplan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionPlanResponse(

        Long id,

        String name,

        String description,

        BigDecimal price,

        Integer durationDays,

        Boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}