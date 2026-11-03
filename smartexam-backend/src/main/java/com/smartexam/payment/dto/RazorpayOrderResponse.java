package com.smartexam.payment.dto;

import java.math.BigDecimal;

public record RazorpayOrderResponse(

        Long paymentId,

        Long subscriptionId,

        String razorpayKeyId,

        String razorpayOrderId,

        BigDecimal amount,

        Long amountInPaise,

        String currency ){
    }