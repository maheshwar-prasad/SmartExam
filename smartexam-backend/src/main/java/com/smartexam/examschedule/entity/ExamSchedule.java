package com.smartexam.examschedule.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "exam_schedules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_exam_schedule_exam_subject",
                        columnNames = {
                                "exam_id",
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
public class ExamSchedule {

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
            name = "exam_subject_id",
            nullable = false
    )
    private Long examSubjectId;

    @Column(
            name = "exam_date",
            nullable = false
    )
    private LocalDate examDate;

    @Column(
            name = "start_time",
            nullable = false
    )
    private LocalTime startTime;

    @Column(
            name = "end_time",
            nullable = false
    )
    private LocalTime endTime;

    /*
     * Optional: examination room.
     * This can later reference a Room/Classroom module.
     */
    @Column(
            name = "room_id"
    )
    private Long roomId;

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
            updatable = false,
            nullable = false
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