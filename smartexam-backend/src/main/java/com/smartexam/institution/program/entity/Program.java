package com.smartexam.institution.program.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.institution.program.enums.DurationUnit;
import com.smartexam.institution.program.enums.ProgramType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "programs",
        indexes = {
                @Index(
                        name = "idx_program_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_program_department",
                        columnList = "department_id"
                ),
                @Index(
                        name = "idx_program_code",
                        columnList = "code"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Program extends BaseEntity {

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Column(
            name = "department_id",
            nullable = false
    )
    private Long departmentId;

    @Column(
            name = "name",
            nullable = false,
            length = 150
    )
    private String name;

    @Column(
            name = "code",
            nullable = false,
            length = 30
    )
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "program_type",
            nullable = false,
            length = 30
    )
    private ProgramType programType;

    @Column(
            name = "duration",
            nullable = false
    )
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "duration_unit",
            nullable = false,
            length = 20
    )
    private DurationUnit durationUnit;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;
}