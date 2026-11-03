package com.smartexam.academiccalendar.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AcademicCalendarResponse(
        Long id,
        Long organizationId,
        Long academicSessionId,
        String academicSessionName,
        String title,
        String eventType,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        String location,
        Boolean allDay,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}