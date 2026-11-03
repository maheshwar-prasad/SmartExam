package com.smartexam.teacher.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.common.enums.Gender;
import com.smartexam.organization.entity.Organization;
import com.smartexam.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

import com.smartexam.employee.entity.Employee;

@Entity
@Table(
        name = "teachers",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_teacher_employee_code_org",
                        columnNames = {
                                "employee_code",
                                "organization_id"
                        }),

                @UniqueConstraint(
                        name = "uk_teacher_email_org",
                        columnNames = {
                                "email",
                                "organization_id"
                        }),

                @UniqueConstraint(
                        name = "uk_teacher_mobile_org",
                        columnNames = {
                                "mobile",
                                "organization_id"
                        })

        },
        indexes = {

                @Index(
                        name = "idx_teacher_org",
                        columnList = "organization_id"),

                @Index(
                        name = "idx_teacher_user",
                        columnList = "user_id"),

                @Index(
                        name = "idx_teacher_active",
                        columnList = "active")

        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Teacher extends BaseEntity {

    @Column(name = "employee_code", nullable = false, length = 30)
    @NotBlank
    @Size(max = 30)
    private String employeeCode;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(length = 200)
    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(length = 200)
    private String specialization;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String mobile;

    @Column(length = 500)
    private String address;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_teacher_organization"))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_teacher_user"))
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            unique = true,
            foreignKey = @ForeignKey(name = "fk_teacher_employee")
    )
    private Employee employee;
}