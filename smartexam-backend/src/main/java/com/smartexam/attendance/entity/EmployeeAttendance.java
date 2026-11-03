package com.smartexam.attendance.entity;

import com.smartexam.attendance.enums.EmployeeAttendanceStatus;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.employee.entity.Employee;
import com.smartexam.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "employee_attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_attendance_date",
                        columnNames = {"employee_id", "attendance_date"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_employee_attendance_org_date",
                        columnList = "organization_id, attendance_date"
                ),
                @Index(
                        name = "idx_employee_attendance_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeAttendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "employee_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_employee_attendance_employee")
    )
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_employee_attendance_organization")
    )
    private Organization organization;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EmployeeAttendanceStatus status;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "remarks", length = 500)
    private String remarks;
}