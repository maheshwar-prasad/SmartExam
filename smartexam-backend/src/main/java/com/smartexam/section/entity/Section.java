package com.smartexam.section.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(
        name = "sections",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_section_code_org",
                        columnNames = {
                                "code",
                                "organization_id"
                        }),

                @UniqueConstraint(
                        name = "uk_section_name_classroom",
                        columnNames = {
                                "name",
                                "class_room_id"
                        })

        },
        indexes = {

                @Index(
                        name = "idx_section_org",
                        columnList = "organization_id"
                ),

                @Index(
                        name = "idx_section_session",
                        columnList = "academic_session_id"
                ),

                @Index(
                        name = "idx_section_classroom",
                        columnList = "class_room_id"
                ),

                @Index(
                        name = "idx_section_active",
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
public class Section extends BaseEntity {

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
            foreignKey = @ForeignKey(name = "fk_section_organization"))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_section_academic_session"))
    private AcademicSession academicSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "class_room_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_section_classroom"))
    private ClassRoom classRoom;

}