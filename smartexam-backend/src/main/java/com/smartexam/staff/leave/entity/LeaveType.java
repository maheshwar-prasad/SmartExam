package com.smartexam.staff.leave.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import com.smartexam.staff.leave.enums.LeaveRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "leave_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_leave_type_code_org",
                        columnNames = {
                                "organization_id",
                                "code"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_leave_type_org",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_leave_type_active",
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
public class LeaveType extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_type_organization"
            )
    )
    private Organization organization;


    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;


    @Column(
            name = "code",
            nullable = false,
            length = 50
    )
    private String code;


    @Column(
            name = "description",
            length = 500
    )
    private String description;


    @Column(
            name = "days_per_year",
            nullable = false,
            precision = 5,
            scale = 2
    )
    @Builder.Default
    private BigDecimal daysPerYear = BigDecimal.ZERO;


    @Column(
            name = "carry_forward_allowed",
            nullable = false
    )
    @Builder.Default
    private Boolean carryForwardAllowed = false;


    @Column(
            name = "max_carry_forward_days",
            precision = 5,
            scale = 2
    )
    private BigDecimal maxCarryForwardDays;


    @Column(
            name = "requires_approval",
            nullable = false
    )
    @Builder.Default
    private Boolean requiresApproval = true;


    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;
}