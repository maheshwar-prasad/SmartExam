package com.smartexam.studenttransfer.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.student.entity.Student;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "student_transfers",
        indexes = {
                @Index(
                        name = "idx_student_transfer_student",
                        columnList = "student_id"
                ),
                @Index(
                        name = "idx_student_transfer_date",
                        columnList = "transfer_date"
                ),
                @Index(
                        name = "idx_student_transfer_source",
                        columnList = "source_enrollment_id"
                ),
                @Index(
                        name = "idx_student_transfer_target",
                        columnList = "target_enrollment_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentTransfer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_student_transfer_student"
            )
    )
    private Student student;


    /*
     * Enrollment from which the student was transferred.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "source_enrollment_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_student_transfer_source_enrollment"
            )
    )
    private StudentEnrollment sourceEnrollment;


    /*
     * Target enrollment.
     *
     * For same-session transfer this can point to the
     * same enrollment because the enrollment is updated
     * in-place.
     *
     * For cross-session transfer this points to the
     * newly created enrollment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "target_enrollment_id",
            foreignKey = @ForeignKey(
                    name = "fk_student_transfer_target_enrollment"
            )
    )
    private StudentEnrollment targetEnrollment;


    @Column(
            name = "source_academic_session_id",
            nullable = false
    )
    private Long sourceAcademicSessionId;


    @Column(
            name = "source_class_room_id",
            nullable = false
    )
    private Long sourceClassRoomId;


    @Column(
            name = "source_section_id",
            nullable = false
    )
    private Long sourceSectionId;


    @Column(
            name = "target_academic_session_id",
            nullable = false
    )
    private Long targetAcademicSessionId;


    @Column(
            name = "target_class_room_id",
            nullable = false
    )
    private Long targetClassRoomId;


    @Column(
            name = "target_section_id",
            nullable = false
    )
    private Long targetSectionId;


    @Column(
            name = "transfer_date",
            nullable = false
    )
    private LocalDate transferDate;


    @Column(
            name = "reason",
            length = 500
    )
    private String reason;


    @Column(
            name = "remarks",
            length = 1000
    )
    private String remarks;
}