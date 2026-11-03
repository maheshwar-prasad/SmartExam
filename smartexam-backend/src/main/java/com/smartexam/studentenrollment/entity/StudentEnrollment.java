package com.smartexam.studentenrollment.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.common.enums.EnrollmentStatus;
import com.smartexam.section.entity.Section;
import com.smartexam.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "student_enrollments",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_student_session",
                        columnNames = {
                                "student_id",
                                "academic_session_id"
                        }),

                @UniqueConstraint(
                        name = "uk_rollno_class_section_session",
                        columnNames = {
                                "roll_no",
                                "class_room_id",
                                "section_id",
                                "academic_session_id"
                        })

        },
        indexes = {

                @Index(
                        name = "idx_student_enrollment_student",
                        columnList = "student_id"),

                @Index(
                        name = "idx_student_enrollment_session",
                        columnList = "academic_session_id"),

                @Index(
                        name = "idx_student_enrollment_classroom",
                        columnList = "class_room_id"),

                @Index(
                        name = "idx_student_enrollment_section",
                        columnList = "section_id"),

                @Index(
                        name = "idx_student_enrollment_active",
                        columnList = "active")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentEnrollment extends BaseEntity {

    @Column(name = "roll_no", nullable = false, length = 20)
    private String rollNo;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", nullable = false, length = 30)
    private EnrollmentStatus enrollmentStatus;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enrollment_student"))
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enrollment_session"))
    private AcademicSession academicSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "class_room_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enrollment_classroom"))
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "section_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enrollment_section"))
    private Section section;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(
            name = "updated_at"
    )
    private LocalDateTime updatedAt;

}