package com.smartexam.alumni.entity;

import com.smartexam.alumni.enums.AlumniStatus;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "alumni",
        indexes = {
                @Index(
                        name = "idx_alumni_student",
                        columnList = "student_id"
                ),
                @Index(
                        name = "idx_alumni_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_alumni_status",
                        columnList = "status"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_alumni_student",
                        columnNames = "student_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Alumni extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_alumni_student"
            )
    )
    private Student student;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Column(name = "leaving_date")
    private LocalDate leavingDate;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    @Column(name = "last_class_name", length = 100)
    private String lastClassName;

    @Column(name = "last_academic_session", length = 100)
    private String lastAcademicSession;

    @Column(name = "higher_education", length = 255)
    private String higherEducation;

    @Column(name = "current_organization", length = 255)
    private String currentOrganization;

    @Column(name = "current_designation", length = 150)
    private String currentDesignation;

    @Column(name = "current_city", length = 100)
    private String currentCity;

    @Column(name = "current_country", length = 100)
    private String currentCountry;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private AlumniStatus status = AlumniStatus.ACTIVE;
}