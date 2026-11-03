package com.smartexam.payment.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.common.enums.PaymentMethod;
import com.smartexam.common.enums.PaymentStatus;
import com.smartexam.organization.entity.Organization;
import com.smartexam.subscription.entity.Subscription;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {

        @Index(name = "idx_payment_org", columnList = "organization_id"),

        @Index(name = "idx_payment_subscription", columnList = "subscription_id"),

        @Index(name = "idx_payment_status", columnList = "status"),

        @Index(name = "idx_payment_transaction", columnList = "transaction_id"),

        @Index(name = "idx_payment_active", columnList = "active")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity {

    /**
     * Organization making the payment.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_organization"))
    private Organization organization;


    /**
     * Subscription associated with this payment.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_subscription"))
    private Subscription subscription;


    /**
     * Payment amount.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;


    /**
     * Payment method.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;


    /**
     * Payment status.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;


    /**
     * External payment gateway transaction ID.
     */
    @Column(name = "transaction_id", unique = true, length = 150)
    private String transactionId;


    /**
     * Gateway response/reference.
     */
    @Column(name = "gateway_reference", length = 500)
    private String gatewayReference;


    /**
     * Payment completion timestamp.
     */
    @Column(name = "paid_at")
    private LocalDateTime paidAt;


    /**
     * Failure reason.
     */
    @Column(name = "failure_reason", length = 500)
    private String failureReason;


    /**
     * Active record flag.
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}