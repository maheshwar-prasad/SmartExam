package com.smartexam.studentidcard.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.student.entity.Student;
import com.smartexam.studentidcard.enums.IdCardStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "student_id_cards",
        indexes = {
                @Index(
                        name = "idx_student_id_card_student",
                        columnList = "student_id"
                ),
                @Index(
                        name = "idx_student_id_card_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_student_id_card_active",
                        columnList = "active"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentIdCard extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_student_id_card_student"
            )
    )
    private Student student;


    @Column(
            name = "card_number",
            nullable = false,
            unique = true,
            length = 100
    )
    private String cardNumber;


    @Column(
            name = "template_name",
            nullable = false,
            length = 100
    )
    private String templateName;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private IdCardStatus status = IdCardStatus.GENERATED;


    @Column(
            name = "academic_session_id"
    )
    private Long academicSessionId;


    @Column(
            name = "storage_path",
            length = 1000
    )
    private String storagePath;


    @Column(
            name = "file_name",
            length = 255
    )
    private String fileName;


    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;


    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;
}