package com.smartexam.academicstructure.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.organization.entity.Organization;
import com.smartexam.section.entity.Section;
import com.smartexam.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "academic_structures",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_academic_structure_code",
                        columnNames = {
                                "organization_id",
                                "academic_session_id",
                                "code"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicStructure extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false
    )
    private Organization organization;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false
    )
    private AcademicSession academicSession;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "class_room_id",
            nullable = false
    )
    private ClassRoom classRoom;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "section_id",
            nullable = false
    )
    private Section section;


    @Column(nullable = false, length = 200)
    private String name;


    @Column(nullable = false, length = 100)
    private String code;


    @Column(length = 1000)
    private String description;


    @Column(nullable = false)
    private Boolean active = true;
}