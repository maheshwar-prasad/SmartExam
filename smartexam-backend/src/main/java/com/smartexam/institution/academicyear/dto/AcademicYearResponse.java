package com.smartexam.institution.academicyear.dto;

import java.time.LocalDate;

public record AcademicYearResponse(

        Long id,

        Long organizationId,

        String name,

        String code,

        LocalDate startDate,

        LocalDate endDate,

        Boolean active,

        Boolean currentYear

) {
}