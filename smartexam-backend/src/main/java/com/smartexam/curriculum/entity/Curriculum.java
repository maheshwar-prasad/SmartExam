package com.smartexam.curriculum.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "curriculums",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_curriculum_org_session_class_code",
                        columnNames = {
                                "organization_id",
                                "academic_session_id",
                                "class_room_id",
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
public class Curriculum extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organization_id",
            nullable = false
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "academic_session_id",
            nullable = false
    )
    private AcademicSession academicSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "class_room_id",
            nullable = false
    )
    private ClassRoom classRoom;

    @Column(
            nullable = false,
            length = 150
    )
    private String name;

    @Column(
            nullable = false,
            length = 50
    )
    private String code;

    @Column(
            length = 500
    )
    private String description;
}