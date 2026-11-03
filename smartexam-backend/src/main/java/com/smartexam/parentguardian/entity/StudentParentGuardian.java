package com.smartexam.parentguardian.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.parentguardian.enums.ParentGuardianRelation;
import com.smartexam.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "student_parent_guardians",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_parent_guardian_relation",
                        columnNames = {
                                "student_id",
                                "parent_guardian_id",
                                "relation"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_spg_student",
                        columnList = "student_id"
                ),
                @Index(
                        name = "idx_spg_parent_guardian",
                        columnList = "parent_guardian_id"
                ),
                @Index(
                        name = "idx_spg_relation",
                        columnList = "relation"
                ),
                @Index(
                        name = "idx_spg_active",
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
public class StudentParentGuardian extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_spg_student"
            )
    )
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "parent_guardian_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_spg_parent_guardian"
            )
    )
    private ParentGuardian parentGuardian;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "relation",
            nullable = false,
            length = 30
    )
    private ParentGuardianRelation relation;

    @Builder.Default
    @Column(
            name = "primary_contact",
            nullable = false
    )
    private Boolean primaryContact = false;

    @Builder.Default
    @Column(
            name = "emergency_contact",
            nullable = false
    )
    private Boolean emergencyContact = false;

    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;

    @Builder.Default
    @Column(
            nullable = false
    )
    private Boolean active = true;
}