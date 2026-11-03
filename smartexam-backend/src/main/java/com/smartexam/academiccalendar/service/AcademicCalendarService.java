package com.smartexam.academiccalendar.service;

import com.smartexam.academiccalendar.dto.AcademicCalendarRequest;
import com.smartexam.academiccalendar.dto.AcademicCalendarResponse;

import java.time.LocalDate;
import java.util.List;

public interface AcademicCalendarService {

    AcademicCalendarResponse create(AcademicCalendarRequest request);

    AcademicCalendarResponse update(Long id, AcademicCalendarRequest request);

    AcademicCalendarResponse getById(Long id);

    List<AcademicCalendarResponse> getAll();

    List<AcademicCalendarResponse> getByOrganization(Long organizationId);

    List<AcademicCalendarResponse> getActiveByOrganization(Long organizationId);

    List<AcademicCalendarResponse> getByAcademicSession(Long sessionId);

    List<AcademicCalendarResponse> getByDateRange(
            Long organizationId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<AcademicCalendarResponse> getByEventType(
            Long organizationId,
            String eventType
    );

    AcademicCalendarResponse activate(Long id);

    AcademicCalendarResponse deactivate(Long id);

    void delete(Long id);
}