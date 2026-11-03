package com.smartexam.academiccalendar.controller;

import com.smartexam.academiccalendar.dto.AcademicCalendarRequest;
import com.smartexam.academiccalendar.dto.AcademicCalendarResponse;
import com.smartexam.academiccalendar.service.AcademicCalendarService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/academic-calendars")
@RequiredArgsConstructor
public class AcademicCalendarController {

    private final AcademicCalendarService calendarService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicCalendarResponse create(
            @Valid @RequestBody AcademicCalendarRequest request
    ) {
        return calendarService.create(request);
    }

    @PutMapping("/{id}")
    public AcademicCalendarResponse update(
            @PathVariable Long id,
            @Valid @RequestBody AcademicCalendarRequest request
    ) {
        return calendarService.update(id, request);
    }

    @GetMapping
    public List<AcademicCalendarResponse> getAll() {
        return calendarService.getAll();
    }

    @GetMapping("/{id}")
    public AcademicCalendarResponse getById(@PathVariable Long id) {
        return calendarService.getById(id);
    }

    @GetMapping("/organization/{organizationId}")
    public List<AcademicCalendarResponse> getByOrganization(
            @PathVariable Long organizationId
    ) {
        return calendarService.getByOrganization(organizationId);
    }

    @GetMapping("/organization/{organizationId}/active")
    public List<AcademicCalendarResponse> getActiveByOrganization(
            @PathVariable Long organizationId
    ) {
        return calendarService.getActiveByOrganization(organizationId);
    }

    @GetMapping("/session/{sessionId}")
    public List<AcademicCalendarResponse> getBySession(
            @PathVariable Long sessionId
    ) {
        return calendarService.getByAcademicSession(sessionId);
    }

    @GetMapping("/organization/{organizationId}/date-range")
    public List<AcademicCalendarResponse> getByDateRange(
            @PathVariable Long organizationId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return calendarService.getByDateRange(
                organizationId, startDate, endDate
        );
    }

    @GetMapping("/organization/{organizationId}/event-type/{eventType}")
    public List<AcademicCalendarResponse> getByEventType(
            @PathVariable Long organizationId,
            @PathVariable String eventType
    ) {
        return calendarService.getByEventType(organizationId, eventType);
    }

    @PatchMapping("/{id}/activate")
    public AcademicCalendarResponse activate(@PathVariable Long id) {
        return calendarService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public AcademicCalendarResponse deactivate(@PathVariable Long id) {
        return calendarService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        calendarService.delete(id);
    }
}