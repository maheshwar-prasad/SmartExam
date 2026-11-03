package com.smartexam.exam.entity;

import com.smartexam.common.enums.ExamStatus;
import com.smartexam.common.enums.ExamType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "exams",
        indexes = {
                @Index(
                        name = "idx_exam_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_exam_academic_session",
                        columnList = "academic_session_id"
                ),
                @Index(
                        name = "idx_exam_class_section",
                        columnList = "class_room_id, section_id"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Setter
    @Column(
            name = "academic_session_id",
            nullable = false
    )
    private Long academicSessionId;

    @Setter
    @Column(
            name = "class_room_id",
            nullable = false
    )
    private Long classRoomId;

    @Setter
    @Column(
            name = "section_id",
            nullable = false
    )
    private Long sectionId;

    @Column(
            name = "name",
            nullable = false,
            length = 150
    )
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "exam_type",
            nullable = false,
            length = 50
    )
    private ExamType examType;

    @Column(
            name = "start_date",
            nullable = false
    )
    private LocalDate startDate;

    @Column(
            name = "end_date",
            nullable = false
    )
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 50
    )
    private ExamStatus status;

    @Column(
            name = "remarks",
            length = 1000
    )
    private String remarks;

    @Column(
            name = "active",
            nullable = false
    )
    private Boolean active = true;

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
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (active == null) {
            active = true;
        }

        if (status == null) {
            status = ExamStatus.DRAFT;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}