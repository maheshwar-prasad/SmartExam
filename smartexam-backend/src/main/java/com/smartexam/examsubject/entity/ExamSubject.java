package com.smartexam.examsubject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "exam_subjects",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_exam_subject",
                        columnNames = {
                                "exam_id",
                                "subject_id"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Column(
            name = "exam_id",
            nullable = false
    )
    private Long examId;

    @Column(
            name = "subject_id",
            nullable = false
    )
    private Long subjectId;

    @Column(
            name = "exam_date",
            nullable = false
    )
    private LocalDate examDate;

    @Column(
            name = "start_time"
    )
    private LocalTime startTime;

    @Column(
            name = "end_time"
    )
    private LocalTime endTime;

    @Column(
            name = "maximum_marks",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal maximumMarks;

    @Column(
            name = "passing_marks",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal passingMarks;

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
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at"
    )
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        if (active == null) {
            active = true;
        }

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();
    }
}