package com.smartexam.payment.dto;

import com.smartexam.common.enums.PaymentMethod;
import com.smartexam.common.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(

        Long id,

        Long organizationId,

        Long subscriptionId,

        BigDecimal amount,

        PaymentMethod paymentMethod,

        PaymentStatus status,

        String transactionId,

        String gatewayReference,

        LocalDateTime paidAt,

        String failureReason,

        Boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt) {
}