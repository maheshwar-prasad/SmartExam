package com.smartexam.staff.leave.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import com.smartexam.teacher.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "leave_balances",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_leave_balance",
                        columnNames = {
                                "organization_id",
                                "teacher_id",
                                "leave_type_id",
                                "academic_session_id"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_leave_balance_teacher",
                        columnList = "teacher_id"
                ),
                @Index(
                        name = "idx_leave_balance_type",
                        columnList = "leave_type_id"
                ),
                @Index(
                        name = "idx_leave_balance_session",
                        columnList = "academic_session_id"
                ),
                @Index(
                        name = "idx_leave_balance_active",
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
public class LeaveBalance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_balance_organization"
            )
    )
    private Organization organization;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_balance_teacher"
            )
    )
    private Teacher teacher;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "leave_type_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_balance_type"
            )
    )
    private LeaveType leaveType;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_balance_session"
            )
    )
    private AcademicSession academicSession;


    @Column(
            name = "allocated_days",
            nullable = false,
            precision = 5,
            scale = 2
    )
    @Builder.Default
    private BigDecimal allocatedDays = BigDecimal.ZERO;


    @Column(
            name = "used_days",
            nullable = false,
            precision = 5,
            scale = 2
    )
    @Builder.Default
    private BigDecimal usedDays = BigDecimal.ZERO;


    @Column(
            name = "pending_days",
            nullable = false,
            precision = 5,
            scale = 2
    )
    @Builder.Default
    private BigDecimal pendingDays = BigDecimal.ZERO;


    @Column(
            name = "remaining_days",
            nullable = false,
            precision = 5,
            scale = 2
    )
    @Builder.Default
    private BigDecimal remainingDays = BigDecimal.ZERO;


    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;
}