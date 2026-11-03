package com.smartexam.academic.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(
        name = "academic_sessions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_academic_session_code_org",
                        columnNames = {
                                "code",
                                "organization_id"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_session_name",
                        columnList = "name"
                ),
                @Index(
                        name = "idx_session_current",
                        columnList = "current_session"
                ),
                @Index(
                        name = "idx_session_org",
                        columnList = "organization_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AcademicSession extends BaseEntity {

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;


    @Column(
            name = "code",
            nullable = false,
            length = 30
    )
    private String code;


    @Column(
            name = "start_date",
            nullable = false
    )
    private LocalDate startDate;


    @Column(
            name = "end_date",
            nullable = false
    )
    private LocalDate endDate;


    @Column(
            name = "current_session",
            nullable = false
    )
    @Builder.Default
    private Boolean currentSession = false;


    @Column(
            name = "description",
            length = 500
    )
    private String description;


    // =========================================================
    // ORGANIZATION
    // =========================================================

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "organization_id",
            nullable = false
    )
    private Organization organization;

}