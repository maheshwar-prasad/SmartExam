package com.smartexam.studentwithdrawal.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.student.entity.Student;
import com.smartexam.studentwithdrawal.enums.WithdrawalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "student_withdrawals",
        indexes = {
                @Index(
                        name = "idx_withdrawal_student",
                        columnList = "student_id"
                ),
                @Index(
                        name = "idx_withdrawal_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_withdrawal_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_withdrawal_date",
                        columnList = "withdrawal_date"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_withdrawal_student",
                        columnNames = "student_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentWithdrawal extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_withdrawal_student"
            )
    )
    private Student student;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Column(
            name = "withdrawal_date",
            nullable = false
    )
    private LocalDate withdrawalDate;

    @Column(
            name = "reason",
            length = 255
    )
    private String reason;

    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;

    @Column(
            name = "last_class_name",
            length = 100
    )
    private String lastClassName;

    @Column(
            name = "last_academic_session",
            length = 100
    )
    private String lastAcademicSession;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private WithdrawalStatus status =
            WithdrawalStatus.ACTIVE;
}