package com.smartexam.subscription.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {

    private Long id;

    private Long organizationId;

    private Long userId;

    private Long planId;

    private String planName;

    private BigDecimal amount;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;

    private Boolean autoRenew;

    private String remarks;
}