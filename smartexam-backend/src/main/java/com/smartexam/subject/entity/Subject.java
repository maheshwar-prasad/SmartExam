package com.smartexam.subject.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "subjects",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_subject_code_org",
                        columnNames = {
                                "code",
                                "organization_id"
                        }),

                @UniqueConstraint(
                        name = "uk_subject_name_org_session",
                        columnNames = {
                                "name",
                                "organization_id",
                                "academic_session_id"
                        })

        },
        indexes = {

                @Index(
                        name = "idx_subject_org",
                        columnList = "organization_id"
                ),

                @Index(
                        name = "idx_subject_session",
                        columnList = "academic_session_id"
                ),

                @Index(
                        name = "idx_subject_active",
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
public class Subject extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(length = 500)
    private String description;

    @Column(name = "theory_marks", nullable = false)
    private Integer theoryMarks;

    @Column(name = "practical_marks", nullable = false)
    private Integer practicalMarks;

    @Column(name = "pass_marks", nullable = false)
    private Integer passMarks;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_subject_organization"))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_subject_session"))
    private AcademicSession academicSession;

}