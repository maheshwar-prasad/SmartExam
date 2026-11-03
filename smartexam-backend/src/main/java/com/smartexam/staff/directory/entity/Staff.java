package com.smartexam.staff.directory.entity;

import com.smartexam.staff.directory.enums.EmploymentType;
import com.smartexam.staff.directory.enums.StaffStatus;
import com.smartexam.staff.directory.enums.StaffType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "staff",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_staff_organization_staff_code",
                        columnNames = {
                                "organization_id",
                                "staff_code"
                        }
                ),
                @UniqueConstraint(
                        name = "uk_staff_organization_employee_code",
                        columnNames = {
                                "organization_id",
                                "employee_code"
                        }
                ),
                @UniqueConstraint(
                        name = "uk_staff_organization_email",
                        columnNames = {
                                "organization_id",
                                "email"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_staff_organization_id",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_staff_department_id",
                        columnList = "department_id"
                ),
                @Index(
                        name = "idx_staff_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_staff_active",
                        columnList = "active"
                ),
                @Index(
                        name = "idx_staff_name",
                        columnList = "first_name,last_name"
                )
        }
)
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Column(
            name = "staff_code",
            nullable = false,
            length = 50
    )
    private String staffCode;

    @Column(
            name = "employee_code",
            length = 50
    )
    private String employeeCode;

    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "middle_name",
            length = 100
    )
    private String middleName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;

    @Column(
            name = "gender",
            length = 20
    )
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(
            name = "email",
            nullable = false,
            length = 150
    )
    private String email;

    @Column(
            name = "phone",
            nullable = false,
            length = 20
    )
    private String phone;

    @Column(
            name = "alternate_phone",
            length = 20
    )
    private String alternatePhone;

    @Column(
            name = "profile_photo_url",
            length = 500
    )
    private String profilePhotoUrl;

    @Column(
            name = "designation",
            nullable = false,
            length = 150
    )
    private String designation;

    @Column(name = "department_id")
    private Long departmentId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "staff_type",
            nullable = false,
            length = 30
    )
    private StaffType staffType;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "employment_type",
            nullable = false,
            length = 30
    )
    private EmploymentType employmentType;

    @Column(
            name = "joining_date",
            nullable = false
    )
    private LocalDate joiningDate;

    @Column(
            name = "qualification",
            length = 255
    )
    private String qualification;

    @Column(
            name = "experience_years",
            precision = 5,
            scale = 2
    )
    private BigDecimal experienceYears;

    @Column(
            name = "address",
            length = 500
    )
    private String address;

    @Column(
            name = "city",
            length = 100
    )
    private String city;

    @Column(
            name = "state",
            length = 100
    )
    private String state;

    @Column(
            name = "country",
            length = 100
    )
    private String country;

    @Column(
            name = "postal_code",
            length = 20
    )
    private String postalCode;

    @Column(
            name = "emergency_contact_name",
            length = 150
    )
    private String emergencyContactName;

    @Column(
            name = "emergency_contact_phone",
            length = 20
    )
    private String emergencyContactPhone;

    @Column(
            name = "emergency_contact_relation",
            length = 50
    )
    private String emergencyContactRelation;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private StaffStatus status = StaffStatus.ACTIVE;

    @Column(
            name = "active",
            nullable = false
    )
    private Boolean active = Boolean.TRUE;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = StaffStatus.ACTIVE;
        }

        if (this.active == null) {
            this.active = Boolean.TRUE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}