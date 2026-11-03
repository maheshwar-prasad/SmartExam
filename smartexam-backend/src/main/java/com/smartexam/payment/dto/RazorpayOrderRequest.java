package com.smartexam.payment.dto;

import jakarta.validation.constraints.NotNull;

public record RazorpayOrderRequest(

        @NotNull(message = "Subscription ID is required")
        Long subscriptionId

) {
}