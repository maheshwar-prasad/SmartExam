package com.smartexam.payment.repository;

import com.smartexam.common.enums.PaymentStatus;
import com.smartexam.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    /**
     * Find active payment.
     */
    Optional<Payment> findByIdAndActiveTrue(Long id);


    /**
     * Find payments for subscription.
     */
    List<Payment> findBySubscriptionIdAndActiveTrue(Long subscriptionId);


    /**
     * Find payments for organization.
     */
    List<Payment> findByOrganizationIdAndActiveTrue(Long organizationId);


    /**
     * Paginated organization payments.
     */
    Page<Payment> findByOrganizationIdAndActiveTrue(Long organizationId, Pageable pageable);


    /**
     * Find payment by transaction ID.
     */
    Optional<Payment> findByTransactionId(String transactionId);


    /**
     * Check transaction ID.
     */
    boolean existsByTransactionId(String transactionId);


    /**
     * Find payments by status.
     */
    List<Payment> findByStatusAndActiveTrue(PaymentStatus status);


    /**
     * Count successful payments.
     */
    long countByStatusAndActiveTrue(PaymentStatus status);
}