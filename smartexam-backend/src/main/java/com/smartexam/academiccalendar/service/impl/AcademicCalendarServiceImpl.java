package com.smartexam.academiccalendar.service.impl;

import com.smartexam.academiccalendar.dto.AcademicCalendarRequest;
import com.smartexam.academiccalendar.dto.AcademicCalendarResponse;
import com.smartexam.academiccalendar.entity.AcademicCalendar;
import com.smartexam.academiccalendar.mapper.AcademicCalendarMapper;
import com.smartexam.academiccalendar.repository.AcademicCalendarRepository;
import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.academiccalendar.service.AcademicCalendarService;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicCalendarServiceImpl implements AcademicCalendarService {

    private final AcademicCalendarRepository calendarRepository;
    private final AcademicSessionRepository sessionRepository;
    private final OrganizationRepository organizationRepository;
    private final AcademicCalendarMapper mapper;

    @Override
    public AcademicCalendarResponse create(AcademicCalendarRequest request) {
        Organization organization = organizationRepository
                .findById(request.organizationId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Organization not found: " + request.organizationId()
                ));

        AcademicSession session = sessionRepository
                .findById(request.academicSessionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Academic session not found: " + request.academicSessionId()
                ));

        validateSessionOrganization(session, organization.getId());
        validateDates(request.startDate(), request.endDate());

        AcademicCalendar entity = new AcademicCalendar();
        entity.setOrganization(organization);
        entity.setAcademicSession(session);

        applyRequest(entity, request);

        return mapper.toResponse(calendarRepository.save(entity));
    }

    @Override
    public AcademicCalendarResponse update(
            Long id,
            AcademicCalendarRequest request
    ) {
        AcademicCalendar entity = findEntity(id);

        if (!entity.getOrganization().getId().equals(request.organizationId())) {
            throw new IllegalArgumentException(
                    "Calendar event does not belong to the specified organization"
            );
        }

        AcademicSession session = sessionRepository
                .findById(request.academicSessionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Academic session not found: " + request.academicSessionId()
                ));

        validateSessionOrganization(session, request.organizationId());
        validateDates(request.startDate(), request.endDate());

        entity.setAcademicSession(session);
        applyRequest(entity, request);

        return mapper.toResponse(calendarRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicCalendarResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getAll() {
        return calendarRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getByOrganization(Long organizationId) {
        return calendarRepository
                .findByOrganization_IdOrderByStartDateAsc(organizationId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getActiveByOrganization(
            Long organizationId
    ) {
        return calendarRepository
                .findByOrganization_IdAndActiveTrueOrderByStartDateAsc(organizationId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getByAcademicSession(Long sessionId) {
        return calendarRepository
                .findByAcademicSession_IdOrderByStartDateAsc(sessionId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getByDateRange(
            Long organizationId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        validateDates(startDate, endDate);

        return calendarRepository
                .findByOrganization_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAsc(
                        organizationId, endDate, startDate
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicCalendarResponse> getByEventType(
            Long organizationId,
            String eventType
    ) {
        return calendarRepository
                .findByOrganization_IdAndEventTypeOrderByStartDateAsc(
                        organizationId, normalizeEventType(eventType)
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public AcademicCalendarResponse activate(Long id) {
        AcademicCalendar entity = findEntity(id);
        entity.setActive(true);
        return mapper.toResponse(calendarRepository.save(entity));
    }

    @Override
    public AcademicCalendarResponse deactivate(Long id) {
        AcademicCalendar entity = findEntity(id);
        entity.setActive(false);
        return mapper.toResponse(calendarRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        calendarRepository.delete(findEntity(id));
    }

    private AcademicCalendar findEntity(Long id) {
        return calendarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Academic calendar event not found: " + id
                ));
    }

    private void applyRequest(
            AcademicCalendar entity,
            AcademicCalendarRequest request
    ) {
        entity.setTitle(request.title().trim());
        entity.setEventType(normalizeEventType(request.eventType()));
        entity.setStartDate(request.startDate());
        entity.setEndDate(request.endDate());
        entity.setDescription(request.description());
        entity.setLocation(request.location());
        entity.setAllDay(request.allDay());
        entity.setActive(request.active());
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Start date and end date are required"
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date"
            );
        }
    }

    private void validateSessionOrganization(
            AcademicSession session,
            Long organizationId
    ) {
        if (session.getOrganization() == null ||
                !session.getOrganization().getId().equals(organizationId)) {
            throw new IllegalArgumentException(
                    "Academic session does not belong to the specified organization"
            );
        }
    }

    private String normalizeEventType(String eventType) {
        return eventType.trim().toUpperCase(Locale.ROOT);
    }
}