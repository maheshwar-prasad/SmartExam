package com.smartexam.timetable.entity;

import com.smartexam.common.enums.DayOfWeek;
import com.smartexam.teacherSubjectAssignment.entity.TeacherSubjectAssignment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(
        name = "timetables",
        indexes = {
                @Index(
                        name = "idx_timetable_assignment",
                        columnList = "teacher_subject_assignment_id"
                ),
                @Index(
                        name = "idx_timetable_day",
                        columnList = "day_of_week"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_subject_assignment_id",
            nullable = false
    )
    private TeacherSubjectAssignment teacherSubjectAssignment;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "day_of_week",
            nullable = false,
            length = 20
    )
    private DayOfWeek dayOfWeek;

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

    @Column(
            name = "room_number",
            length = 100
    )
    private String roomNumber;

    @Column(
            name = "active",
            nullable = false
    )
    private Boolean active;
}