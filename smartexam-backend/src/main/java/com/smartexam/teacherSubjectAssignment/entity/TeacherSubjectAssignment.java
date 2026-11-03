package com.smartexam.teacherSubjectAssignment.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.section.entity.Section;
import com.smartexam.subject.entity.Subject;
import com.smartexam.teacher.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "teacher_subject_assignments",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_teacher_subject_assignment",
                        columnNames = {
                                "teacher_id",
                                "subject_id",
                                "class_room_id",
                                "section_id",
                                "academic_session_id"
                        })

        },
        indexes = {

                @Index(
                        name = "idx_teacher_assignment_teacher",
                        columnList = "teacher_id"),

                @Index(
                        name = "idx_teacher_assignment_subject",
                        columnList = "subject_id"),

                @Index(
                        name = "idx_teacher_assignment_classroom",
                        columnList = "class_room_id"),

                @Index(
                        name = "idx_teacher_assignment_section",
                        columnList = "section_id"),

                @Index(
                        name = "idx_teacher_assignment_session",
                        columnList = "academic_session_id"),

                @Index(
                        name = "idx_teacher_assignment_active",
                        columnList = "active")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TeacherSubjectAssignment extends BaseEntity {

    @Builder.Default
    @Column(name = "class_teacher", nullable = false)
    private Boolean classTeacher = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_teacher"))
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "subject_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_subject"))
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "class_room_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_classroom"))
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "section_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_section"))
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_session"))
    private AcademicSession academicSession;

}