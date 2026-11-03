package com.smartexam.subscription.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.common.enums.SubscriptionStatus;
import com.smartexam.organization.entity.Organization;
import com.smartexam.subscriptionplan.entity.SubscriptionPlan;
import com.smartexam.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "subscriptions",
        indexes = {

                @Index(
                        name = "idx_subscription_org",
                        columnList = "organization_id"
                ),

                @Index(
                        name = "idx_subscription_plan",
                        columnList = "plan_id"
                ),

                @Index(
                        name = "idx_subscription_status",
                        columnList = "status"
                ),

                @Index(
                        name = "idx_subscription_end_date",
                        columnList = "end_date"
                ),

                @Index(
                        name = "idx_subscription_active",
                        columnList = "active"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_subscription_organization"
            )
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "plan_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_subscription_plan"
            )
    )
    private SubscriptionPlan plan;

    @Column(
            name = "start_date",
            nullable = false
    )
    private LocalDate startDate;

    @Column(
            name = "end_date",
            nullable = false
    )
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private SubscriptionStatus status;

    @Column(
            precision = 10,
            scale = 2,
            nullable = false
    )
    private BigDecimal amount;

    @Builder.Default
    @Column(
            nullable = false
    )
    private Boolean active = true;

    @PrePersist
    protected void onCreate() {

        if (active == null) {
            active = true;
        }

        if (status == null) {
            status = SubscriptionStatus.PENDING;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by_user_id",
            foreignKey = @ForeignKey(
                    name = "fk_subscription_created_by_user"
            )
    )
    private User createdByUser;

    @Column(name = "auto_renew", nullable = false)
    @Builder.Default
    private Boolean autoRenew = false;
    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;
}