package com.smartexam.payment.dto;

import com.smartexam.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(

        @NotNull(message = "Subscription ID is required")
        Long subscriptionId,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod) {
}