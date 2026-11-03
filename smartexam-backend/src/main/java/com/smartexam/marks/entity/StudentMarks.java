package com.smartexam.marks.entity;

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
        name = "student_marks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_exam_subject",
                        columnNames = {
                                "student_id",
                                "exam_subject_id"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentMarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
            name = "exam_subject_id",
            nullable = false
    )
    private Long examSubjectId;

    @Column(
            name = "maximum_marks",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal maximumMarks;

    @Column(
            name = "obtained_marks",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal obtainedMarks;

    @Column(
            name = "grade",
            length = 10
    )
    private String grade;

    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;

    @Column(
            nullable = false
    )
    private Boolean published;

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

        if (published == null) {
            published = false;
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

    @Column(
            name = "pass_marks",
            precision = 10,
            scale = 2,
            nullable = false
    )
    private BigDecimal passMarks;
}