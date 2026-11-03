package com.smartexam.payment.service;

import com.smartexam.payment.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {

    PaymentResponse create(PaymentRequest request);

    PaymentResponse getById(Long id);

    List<PaymentResponse> getBySubscription(Long subscriptionId);

    List<PaymentResponse> getByOrganization(Long organizationId);

    Page<PaymentResponse> getByOrganization(Long organizationId, Pageable pageable);

    PaymentResponse processMockPayment(Long paymentId, boolean success);

    PaymentResponse markPaymentSuccess(Long paymentId);

    RazorpayOrderResponse createRazorpayOrder(
            RazorpayOrderRequest request
    );

    PaymentResponse verifyRazorpayPayment(
            RazorpayVerifyRequest request
    );
}