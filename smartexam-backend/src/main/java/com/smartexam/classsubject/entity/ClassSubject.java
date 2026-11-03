package com.smartexam.classsubject.entity;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import com.smartexam.subject.entity.Subject;
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
        name = "class_subjects",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_class_subject",
                        columnNames = {
                                "organization_id",
                                "academic_session_id",
                                "class_room_id",
                                "subject_id"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSubject extends BaseEntity {

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
            name = "subject_id",
            nullable = false
    )
    private Subject subject;

    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;
}