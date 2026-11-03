package com.smartexam.institution.academicyear.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AcademicYearRequest(

        @NotNull(message = "Organization ID must not be null")
        Long organizationId,

        @NotBlank(message = "Academic year name is required")
        @Size(
                max = 50,
                message = "Academic year name must not exceed 50 characters"
        )
        String name,

        @NotBlank(message = "Academic year code is required")
        @Size(
                max = 50,
                message = "Academic year code must not exceed 50 characters"
        )
        String code,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        @NotNull(message = "Active status is required")
        Boolean active,

        @NotNull(message = "Current year status is required")
        Boolean currentYear

) {

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