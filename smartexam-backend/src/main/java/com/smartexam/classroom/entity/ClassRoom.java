package com.smartexam.classroom.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
@SuperBuilder
@Entity
@Table(
        name = "class_rooms",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_classroom_code_org",
                        columnNames = {"code", "organization_id"}
                ),
                @UniqueConstraint(
                        name = "uk_classroom_name_org",
                        columnNames = {"name", "organization_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_classroom_org",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_classroom_session",
                        columnList = "academic_session_id"
                ),
                @Index(
                        name = "idx_classroom_active",
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
public class ClassRoom extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(nullable = false)
    private Integer capacity;

    @Column(length = 500)
    private String description;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_classroom_organization")
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_classroom_academic_session")
    )
    private AcademicSession academicSession;

}