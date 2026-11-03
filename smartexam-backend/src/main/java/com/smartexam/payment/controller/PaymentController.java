package com.smartexam.payment.controller;

import com.smartexam.payment.dto.*;
import com.smartexam.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    /**
     * Create payment.
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(request));
    }


    /**
     * Get payment by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(paymentService.getById(id));
    }


    /**
     * Get payments by subscription.
     */
    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<PaymentResponse>> getBySubscription(@PathVariable Long subscriptionId) {

        return ResponseEntity.ok(paymentService.getBySubscription(subscriptionId));
    }


    /**
     * Get payments by organization.
     */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<PaymentResponse>> getByOrganization(@PathVariable Long organizationId) {

        return ResponseEntity.ok(paymentService.getByOrganization(organizationId));
    }


    /**
     * Paginated organization payments.
     */
    @GetMapping("/organization/{organizationId}/page")
    public ResponseEntity<Page<PaymentResponse>> getByOrganizationPage(@PathVariable Long organizationId, Pageable pageable) {

        return ResponseEntity.ok(paymentService.getByOrganization(organizationId, pageable));
    }


    /**
     * Mock payment processing.
     * <p>
     * Example:
     * <p>
     * POST /api/v1/payments/1/mock?success=true
     */
    @PostMapping("/{id}/mock")
    public ResponseEntity<PaymentResponse> processMockPayment(@PathVariable Long id, @RequestParam boolean success) {

        return ResponseEntity.ok(paymentService.processMockPayment(id, success));
    }

    /**
     * Mark payment as successful.
     *
     * PUT /api/v1/payments/{id}/success
     */
    @PutMapping("/{id}/success")
    public ResponseEntity<PaymentResponse> markPaymentSuccess(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.markPaymentSuccess(id)
        );
    }

    @PostMapping("/order")
    public ResponseEntity<RazorpayOrderResponse> createRazorpayOrder(
            @Valid @RequestBody RazorpayOrderRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.createRazorpayOrder(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyRazorpayPayment(
            @Valid @RequestBody RazorpayVerifyRequest request) {

        return ResponseEntity.ok(
                paymentService.verifyRazorpayPayment(request)
        );
    }
}