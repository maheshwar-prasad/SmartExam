package com.smartexam.academiccalendar.repository;

import com.smartexam.academiccalendar.entity.AcademicCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AcademicCalendarRepository
        extends JpaRepository<AcademicCalendar, Long> {

    List<AcademicCalendar> findByOrganization_IdOrderByStartDateAsc(
            Long organizationId
    );

    List<AcademicCalendar>
    findByOrganization_IdAndActiveTrueOrderByStartDateAsc(
            Long organizationId
    );

    List<AcademicCalendar>
    findByAcademicSession_IdOrderByStartDateAsc(
            Long academicSessionId
    );

    List<AcademicCalendar>
    findByOrganization_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAsc(
            Long organizationId,
            LocalDate endDate,
            LocalDate startDate
    );

    List<AcademicCalendar>
    findByOrganization_IdAndEventTypeOrderByStartDateAsc(
            Long organizationId,
            String eventType
    );
}