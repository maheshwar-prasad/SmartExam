package com.smartexam.payment.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.smartexam.common.enums.PaymentMethod;
import com.smartexam.common.enums.PaymentStatus;
import com.smartexam.common.enums.SubscriptionStatus;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.payment.config.RazorpayProperties;
import com.smartexam.payment.dto.*;
import com.smartexam.payment.entity.Payment;
import com.smartexam.payment.event.PaymentNotificationPublisher;
import com.smartexam.payment.mapper.PaymentMapper;
import com.smartexam.payment.repository.PaymentRepository;
import com.smartexam.payment.service.PaymentService;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.subscription.entity.Subscription;
import com.smartexam.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentNotificationPublisher paymentNotificationPublisher;
    private final CurrentUserService currentUserService;
    private final RazorpayClient razorpayClient;
    private final RazorpayProperties razorpayProperties;

    /**
     * Create a payment.
     */
    @Override
    public PaymentResponse create(PaymentRequest request) {

        /*
         * 1. Find subscription.
         */
        Subscription subscription = subscriptionRepository.findByIdAndActiveTrue(request.subscriptionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription not found with id: "
                                        + request.subscriptionId()));

        Long organizationId = currentUserService.getOrganizationId();

        if (!subscription.getOrganization().getId().equals(organizationId)) {
            throw new AccessDeniedException(
                    "You are not authorized to create payment for this subscription"
            );
        }
        /*
         * 2. Validate subscription status.
         *
         * Payment should normally be created
         * only for PENDING subscriptions.
         */
        if (subscription.getStatus() != SubscriptionStatus.PENDING) {

            throw new IllegalArgumentException("Payment can only be created " + "for a PENDING subscription.");
        }


        /*
         * 3. Prevent duplicate pending payment.
         */
        List<Payment> existingPayments = paymentRepository.findBySubscriptionIdAndActiveTrue(subscription.getId());

        boolean paymentExists = existingPayments.stream().anyMatch(payment -> payment.getStatus() == PaymentStatus.CREATED || payment.getStatus() == PaymentStatus.PENDING);

        if (paymentExists) {

            throw new IllegalArgumentException("A pending payment already exists " + "for this subscription.");
        }


        /*
         * 4. Organization comes from subscription.
         */
        Organization organization = subscription.getOrganization();


        /*
         * 5. Create payment.
         *
         * Amount is taken from subscription.
         */
        Payment payment = Payment.builder().organization(organization).subscription(subscription).amount(subscription.getAmount()).paymentMethod(request.paymentMethod()).status(PaymentStatus.CREATED).active(true).build();


        /*
         * 6. Save payment.
         */
        Payment saved = paymentRepository.save(payment);


        return mapToResponse(saved);
    }


    /**
     * Get payment.
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getById(Long id) {

        Payment payment = paymentRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        return mapToResponse(payment);
    }


    /**
     * Get payments by subscription.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getBySubscription(Long subscriptionId) {

        return paymentRepository.findBySubscriptionIdAndActiveTrue(subscriptionId).stream().map(this::mapToResponse).toList();
    }


    /**
     * Get payments by organization.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getByOrganization(Long organizationId) {

        return paymentRepository.findByOrganizationIdAndActiveTrue(organizationId).stream().map(this::mapToResponse).toList();
    }


    /**
     * Paginated organization payments.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getByOrganization(Long organizationId, Pageable pageable) {

        return paymentRepository.findByOrganizationIdAndActiveTrue(organizationId, pageable).map(this::mapToResponse);
    }


    /**
     * Mock payment processing.
     * <p>
     * This method simulates a payment gateway.
     * <p>
     * success = true
     * Payment SUCCESS
     * Subscription ACTIVE
     * <p>
     * success = false
     * Payment FAILED
     * Subscription remains PENDING
     */
    @Override
    public PaymentResponse processMockPayment(Long paymentId, boolean success) {

        Payment payment = paymentRepository.findByIdAndActiveTrue(paymentId).orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));


        /*
         * Payment must be CREATED or PENDING.
         */
        if (payment.getStatus() != PaymentStatus.CREATED && payment.getStatus() != PaymentStatus.PENDING) {

            throw new IllegalArgumentException("Payment cannot be processed in status: " + payment.getStatus());
        }
        payment.setStatus(PaymentStatus.PENDING);
        if (success) {
            String transactionId = "MOCK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();

            payment.setTransactionId(transactionId);

            payment.setGatewayReference("MOCK_PAYMENT_SUCCESS");

            payment.setStatus(PaymentStatus.SUCCESS);

            payment.setPaidAt(LocalDateTime.now());
            /*
             * Activate subscription.
             */
            Subscription subscription = payment.getSubscription();

            subscription.setStatus(SubscriptionStatus.ACTIVE);

            subscription.setActive(true);

            subscriptionRepository.save(subscription);


            /*
             * Send notification.
             */
            if (subscription.getCreatedByUser() != null) {

                paymentNotificationPublisher.publishSubscriptionActivated(subscription.getCreatedByUser().getId(),

                        subscription.getOrganization().getId(),

                        subscription.getPlan().getName());
            }
        } else {

            payment.setStatus(PaymentStatus.FAILED);

            payment.setGatewayReference("MOCK_PAYMENT_FAILED");

            payment.setFailureReason("Mock payment declined.");


            Subscription subscription = payment.getSubscription();


            if (subscription.getCreatedByUser() != null) {

                paymentNotificationPublisher.publishPaymentFailed(subscription.getCreatedByUser().getId(),

                        subscription.getOrganization().getId());
            }
        }


        Payment saved = paymentRepository.save(payment);

        return mapToResponse(saved);
    }


    private PaymentResponse mapToResponse(Payment payment) {

        return new PaymentResponse(

                payment.getId(),

                payment.getOrganization().getId(),

                payment.getSubscription().getId(),

                payment.getAmount(),

                payment.getPaymentMethod(),

                payment.getStatus(),

                payment.getTransactionId(),

                payment.getGatewayReference(),

                payment.getPaidAt(),

                payment.getFailureReason(),

                payment.getActive(),

                payment.getCreatedAt(),

                payment.getUpdatedAt());
    }

    @Transactional
    @Override
    public PaymentResponse markPaymentSuccess(Long paymentId) {

        Long organizationId = currentUserService.getOrganizationId();

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + paymentId));

        if (!payment.getSubscription()
                .getOrganization()
                .getId()
                .equals(organizationId)) {

            throw new AccessDeniedException(
                    "You are not authorized to update this payment");
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return mapper.toResponse(payment);
        }

        if (payment.getStatus() != PaymentStatus.CREATED) {
            throw new IllegalStateException(
                    "Only CREATED payments can be marked as successful");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        payment.setTransactionId(
                "TXN-" + UUID.randomUUID()
        );

        payment.setGatewayReference(
                "GW-" + UUID.randomUUID()
        );

        Payment savedPayment = paymentRepository.save(payment);

        Subscription subscription = payment.getSubscription();

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setActive(true);

        subscriptionRepository.save(subscription);
        /*
         * Send subscription activation notification.
         */
        if (subscription.getCreatedByUser() != null) {
            paymentNotificationPublisher.publishSubscriptionActivated(
                    subscription.getCreatedByUser().getId(),
                    subscription.getOrganization().getId(),
                    subscription.getPlan().getName()
            );
        }
        return mapper.toResponse(savedPayment);
    }

    @Override
    @Transactional
    public RazorpayOrderResponse createRazorpayOrder(
            RazorpayOrderRequest request) {

        Long organizationId = currentUserService.getOrganizationId();

        Subscription subscription =
                subscriptionRepository
                        .findByIdAndActiveTrue(request.subscriptionId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subscription not found with id: "
                                                + request.subscriptionId()));

        if (!subscription.getOrganization()
                .getId()
                .equals(organizationId)) {

            throw new AccessDeniedException(
                    "You are not authorized to pay for this subscription"
            );
        }

        if (subscription.getStatus() != SubscriptionStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING subscriptions can be paid"
            );
        }

        BigDecimal amount = subscription.getAmount();

        long amountInPaise = amount
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        try {

            JSONObject orderRequest = new JSONObject();

            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            String receipt = "SUB-" + subscription.getId();
            orderRequest.put("receipt", receipt);

            Order razorpayOrder =
                    razorpayClient.orders.create(orderRequest);

            String razorpayOrderId =
                    razorpayOrder.get("id");

            Payment payment = Payment.builder()
                    .organization(subscription.getOrganization())
                    .subscription(subscription)
                    .amount(amount)
                    .paymentMethod(PaymentMethod.CARD)
                    .status(PaymentStatus.CREATED)
                    .gatewayReference(razorpayOrderId)
                    .active(true)
                    .build();

            Payment savedPayment =
                    paymentRepository.save(payment);

            return new RazorpayOrderResponse(
                    savedPayment.getId(),
                    subscription.getId(),
                    razorpayProperties.keyId(),
                    razorpayOrderId,
                    amount,
                    amountInPaise,
                    "INR"
            );
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Unable to create Razorpay order",
                    ex
            );
        }
    }
    @Override
    @Transactional
    public PaymentResponse verifyRazorpayPayment(
            RazorpayVerifyRequest request) {

        Long organizationId = currentUserService.getOrganizationId();

        /*
         * 1. Find payment using Razorpay Order ID.
         *
         * gatewayReference contains the Razorpay order ID
         * created during createRazorpayOrder().
         */
        Payment payment = paymentRepository
                .findByOrganizationIdAndActiveTrue(organizationId)
                .stream()
                .filter(p ->
                        request.razorpayOrderId()
                                .equals(p.getGatewayReference()))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for Razorpay order: "
                                        + request.razorpayOrderId()
                        ));

        /*
         * 2. Payment must be CREATED.
         */
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return mapper.toResponse(payment);
        }

        if (payment.getStatus() != PaymentStatus.CREATED) {
            throw new IllegalStateException(
                    "Payment cannot be verified in status: "
                            + payment.getStatus());
        }

        /*
         * 3. Verify that the Razorpay order belongs
         *    to the payment stored in our database.
         */
        if (!request.razorpayOrderId()
                .equals(payment.getGatewayReference())) {

            throw new IllegalArgumentException(
                    "Razorpay order ID does not match payment");
        }

        /*
         * 4. Verify Razorpay signature.
         *
         * Razorpay:
         *
         * HMAC_SHA256(
         *      razorpay_order_id + "|" + razorpay_payment_id,
         *      key_secret
         * )
         */
        try {

            JSONObject options = new JSONObject();

            options.put(
                    "razorpay_order_id",
                    request.razorpayOrderId());

            options.put(
                    "razorpay_payment_id",
                    request.razorpayPaymentId());

            options.put(
                    "razorpay_signature",
                    request.razorpaySignature());

            boolean signatureValid =
                    Utils.verifyPaymentSignature(
                            options,
                            razorpayProperties.keySecret()
                    );

            if (!signatureValid) {

                payment.setStatus(PaymentStatus.FAILED);

                payment.setFailureReason(
                        "Invalid Razorpay payment signature"
                );

                paymentRepository.save(payment);

                throw new SecurityException(
                        "Invalid Razorpay payment signature"
                );
            }

        } catch (SecurityException ex) {
            throw ex;

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Unable to verify Razorpay payment",
                    ex
            );
        }

        /*
         * 5. Signature is valid.
         *
         * Store Razorpay payment ID.
         */
        payment.setTransactionId(
                request.razorpayPaymentId()
        );

        /*
         * gatewayReference already contains
         * Razorpay Order ID.
         */
        payment.setGatewayReference(
                request.razorpayOrderId()
        );

        payment.setStatus(PaymentStatus.SUCCESS);

        payment.setPaidAt(LocalDateTime.now());

        payment.setFailureReason(null);

        /*
         * 6. Activate subscription.
         */
        Subscription subscription =
                payment.getSubscription();

        subscription.setStatus(
                SubscriptionStatus.ACTIVE
        );

        subscription.setActive(true);

        subscriptionRepository.save(subscription);

        /*
         * 7. Save payment.
         */
        Payment savedPayment =
                paymentRepository.save(payment);

        /*
         * 8. Send notification.
         */
        if (subscription.getCreatedByUser() != null) {

            paymentNotificationPublisher
                    .publishSubscriptionActivated(
                            subscription
                                    .getCreatedByUser()
                                    .getId(),

                            subscription
                                    .getOrganization()
                                    .getId(),

                            subscription
                                    .getPlan()
                                    .getName()
                    );
        }

        /*
         * 9. Return response.
         */
        return mapper.toResponse(savedPayment);
    }

}