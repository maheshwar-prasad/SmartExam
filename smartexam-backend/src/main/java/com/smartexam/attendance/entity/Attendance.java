package com.smartexam.attendance.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.common.enums.AttendanceStatus;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import com.smartexam.teacherSubjectAssignment.entity.TeacherSubjectAssignment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_attendance",
                        columnNames = {
                                "attendance_date",
                                "student_enrollment_id",
                                "teacher_subject_assignment_id"
                        })

        },
        indexes = {

                @Index(
                        name = "idx_attendance_student",
                        columnList = "student_enrollment_id"),

                @Index(
                        name = "idx_attendance_teacher_subject",
                        columnList = "teacher_subject_assignment_id"),

                @Index(
                        name = "idx_attendance_date",
                        columnList = "attendance_date"),

                @Index(
                        name = "idx_attendance_active",
                        columnList = "active")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Attendance extends BaseEntity {

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;

    @Column(length = 500)
    private String remarks;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_enrollment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attendance_student"))
    private StudentEnrollment studentEnrollment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_subject_assignment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attendance_teacher_subject"))
    private TeacherSubjectAssignment teacherSubjectAssignment;

}