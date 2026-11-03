package com.smartexam.examregistration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "student_exam_registrations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_exam_registration",
                        columnNames = {
                                "student_id",
                                "exam_id"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentExamRegistration {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Column(
            name = "student_id",
            nullable = false
    )
    private Long studentId;

    @Column(
            name = "exam_id",
            nullable = false
    )
    private Long examId;

    @Column(
            name = "registration_number",
            nullable = false,
            unique = true,
            length = 100
    )
    private String registrationNumber;

    @Column(
            name = "registration_date",
            nullable = false
    )
    private LocalDate registrationDate;

    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private String status;

    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;

    @Column(
            nullable = false
    )
    private Boolean active;

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
    public void prePersist() {

        if (active == null) {
            active = true;
        }

        if (status == null) {
            status = "REGISTERED";
        }

        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();
    }
}