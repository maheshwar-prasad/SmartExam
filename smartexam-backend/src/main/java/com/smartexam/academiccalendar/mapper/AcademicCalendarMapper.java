package com.smartexam.academiccalendar.mapper;

import com.smartexam.academiccalendar.dto.AcademicCalendarResponse;
import com.smartexam.academiccalendar.entity.AcademicCalendar;
import org.springframework.stereotype.Component;

@Component
public class AcademicCalendarMapper {

    public AcademicCalendarResponse toResponse(AcademicCalendar entity) {
        return new AcademicCalendarResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getAcademicSession().getId(),
                entity.getAcademicSession().getName(),
                entity.getTitle(),
                entity.getEventType(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getDescription(),
                entity.getLocation(),
                entity.getAllDay(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}