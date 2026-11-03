package com.smartexam.academiccalendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AcademicCalendarRequest(
        @NotNull
        Long organizationId,

        @NotNull
        Long academicSessionId,

        @NotBlank
        @Size(max = 200)
        String title,

        @NotBlank
        @Size(max = 50)
        String eventType,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate,

        @Size(max = 1000)
        String description,

        @Size(max = 200)
        String location,

        @NotNull
        Boolean allDay,

        @NotNull
        Boolean active
) {}