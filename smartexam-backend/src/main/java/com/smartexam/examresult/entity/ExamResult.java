package com.smartexam.examresult.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "exam_results",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_exam_result_student_exam",
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
public class ExamResult {

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
            name = "total_subjects",
            nullable = false
    )
    private Integer totalSubjects;

    @Column(
            name = "total_maximum_marks",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal totalMaximumMarks;

    @Column(
            name = "total_obtained_marks",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal totalObtainedMarks;

    @Column(
            precision = 5,
            scale = 2
    )
    private BigDecimal percentage;

    @Column(
            length = 10
    )
    private String grade;

    @Column(
            name = "result_status",
            nullable = false,
            length = 30
    )
    private String resultStatus;

    @Column(
            nullable = false
    )
    private Boolean published;

    @Column(
            nullable = false
    )
    private Boolean active;

    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;

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

        if (published == null) {
            published = false;
        }

        if (active == null) {
            active = true;
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