package com.smartexam.institution.academicyear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "academic_years",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_academic_year_org_code",
                        columnNames = {
                                "organization_id",
                                "code"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @Column(
            nullable = false,
            length = 50
    )
    private String name;

    @Column(
            nullable = false,
            length = 50
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
            nullable = false
    )
    private Boolean active = true;

    @Column(
            name = "current_year",
            nullable = false
    )
    private Boolean currentYear = false;

    @AssertTrue(
            message = "End date must be after start date"
    )
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }

        return endDate.isAfter(startDate);
    }
}