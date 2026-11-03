package com.smartexam.employee.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.common.enums.Gender;
import com.smartexam.employee.enums.EmployeeType;
import com.smartexam.organization.entity.Organization;
import com.smartexam.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_code_org",
                        columnNames = {"organization_id", "employee_code"}
                ),
                @UniqueConstraint(
                        name = "uk_employee_user_org",
                        columnNames = {"organization_id", "user_id"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Employee extends BaseEntity {

    @Column(name = "employee_code", nullable = false, length = 30)
    private String employeeCode;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type", nullable = false, length = 30)
    private EmployeeType employeeType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String mobile;

    @Column(length = 500)
    private String address;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}