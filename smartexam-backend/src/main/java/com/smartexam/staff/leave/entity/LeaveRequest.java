package com.smartexam.staff.leave.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import com.smartexam.staff.leave.enums.LeaveRequestStatus;
import com.smartexam.teacher.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "leave_requests",
        indexes = {
                @Index(
                        name = "idx_leave_request_teacher",
                        columnList = "teacher_id"
                ),
                @Index(
                        name = "idx_leave_request_type",
                        columnList = "leave_type_id"
                ),
                @Index(
                        name = "idx_leave_request_session",
                        columnList = "academic_session_id"
                ),
                @Index(
                        name = "idx_leave_request_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_leave_request_dates",
                        columnList = "from_date,to_date"
                ),
                @Index(
                        name = "idx_leave_request_active",
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
public class LeaveRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_request_organization"
            )
    )
    private Organization organization;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_request_teacher"
            )
    )
    private Teacher teacher;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "leave_type_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_request_type"
            )
    )
    private LeaveType leaveType;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_leave_request_session"
            )
    )
    private AcademicSession academicSession;


    @Column(
            name = "from_date",
            nullable = false
    )
    private LocalDate fromDate;


    @Column(
            name = "to_date",
            nullable = false
    )
    private LocalDate toDate;


    @Column(
            name = "number_of_days",
            nullable = false,
            precision = 5,
            scale = 2
    )
    private BigDecimal numberOfDays;


    @Column(
            name = "reason",
            nullable = false,
            length = 1000
    )
    private String reason;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private LeaveRequestStatus status =
            LeaveRequestStatus.PENDING;


    @Column(
            name = "applied_at",
            nullable = false
    )
    private LocalDateTime appliedAt;


    @Column(
            name = "approved_by"
    )
    private Long approvedBy;


    @Column(
            name = "approved_at"
    )
    private LocalDateTime approvedAt;


    @Column(
            name = "rejected_by"
    )
    private Long rejectedBy;


    @Column(
            name = "rejected_at"
    )
    private LocalDateTime rejectedAt;


    @Column(
            name = "approval_comments",
            length = 1000
    )
    private String approvalComments;


    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;


    @PrePersist
    protected void onCreate() {

        if (appliedAt == null) {
            appliedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = LeaveRequestStatus.PENDING;
        }

        if (active == null) {
            active = true;
        }
    }
}