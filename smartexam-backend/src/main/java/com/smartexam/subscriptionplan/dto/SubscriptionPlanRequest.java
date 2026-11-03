package com.smartexam.subscriptionplan.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SubscriptionPlanRequest(

        @NotBlank(
                message = "Plan name is required"
        )
        @Size(
                max = 50,
                message = "Plan name cannot exceed 50 characters"
        )
        String name,

        @Size(
                max = 500,
                message = "Description cannot exceed 500 characters"
        )
        String description,

        @NotNull(
                message = "Price is required"
        )
        @DecimalMin(
                value = "0.00",
                message = "Price cannot be negative"
        )
        BigDecimal price,

        @NotNull(
                message = "Duration is required"
        )
        @Min(
                value = 1,
                message = "Duration must be at least 1 day"
        )
        Integer durationDays
) {
}