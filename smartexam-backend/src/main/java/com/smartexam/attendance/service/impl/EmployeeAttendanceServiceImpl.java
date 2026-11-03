package com.smartexam.attendance.service.impl;

import com.smartexam.attendance.dto.*;
import com.smartexam.attendance.entity.EmployeeAttendance;
import com.smartexam.attendance.enums.EmployeeAttendanceStatus;
import com.smartexam.attendance.mapper.EmployeeAttendanceMapper;
import com.smartexam.attendance.repository.EmployeeAttendanceRepository;
import com.smartexam.attendance.service.EmployeeAttendanceService;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.employee.entity.Employee;
import com.smartexam.employee.repository.EmployeeRepository;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeAttendanceServiceImpl
        implements EmployeeAttendanceService {

    private final EmployeeAttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final EmployeeAttendanceMapper attendanceMapper;

    @Override
    public EmployeeAttendanceResponse markAttendance(
            EmployeeAttendanceRequest request) {

        validateRequest(request);

        if (attendanceRepository.existsByEmployee_IdAndAttendanceDate(
                request.getEmployeeId(),
                request.getAttendanceDate())) {

            throw new DuplicateResourceException(
                    "Attendance already exists for this employee on ", request.getAttendanceDate().toString());
        }

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: "
                                + request.getEmployeeId()));

        if (!employee.getOrganization().getId()
                .equals(request.getOrganizationId())) {
            throw new IllegalArgumentException(
                    "Employee does not belong to the specified organization");
        }

        if (!Boolean.TRUE.equals(employee.getActive())) {
            throw new IllegalArgumentException(
                    "Attendance cannot be marked for an inactive employee");
        }

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organization not found: "
                                + request.getOrganizationId()));

        EmployeeAttendance attendance = new EmployeeAttendance();
        attendance.setEmployee(employee);
        attendance.setOrganization(organization);

        applyRequest(attendance, request);

        return attendanceMapper.toResponse(
                attendanceRepository.save(attendance));
    }

    @Override
    public EmployeeAttendanceResponse updateAttendance(
            Long attendanceId,
            EmployeeAttendanceRequest request) {

        validateRequest(request);

        EmployeeAttendance attendance = attendanceRepository
                .findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee attendance not found: " + attendanceId));

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + request.getEmployeeId()));

        if (!employee.getOrganization().getId()
                .equals(request.getOrganizationId())) {
            throw new IllegalArgumentException(
                    "Employee does not belong to the specified organization");
        }

        if (!attendance.getOrganization().getId()
                .equals(request.getOrganizationId())) {
            throw new IllegalArgumentException(
                    "Attendance does not belong to the specified organization");
        }

        attendanceRepository
                .findByEmployee_IdAndAttendanceDate(
                        request.getEmployeeId(),
                        request.getAttendanceDate())
                .filter(existing -> !existing.getId().equals(attendanceId))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Attendance already exists for this employee on ", request.getAttendanceDate().toString());
                });

        attendance.setEmployee(employee);
        applyRequest(attendance, request);

        return attendanceMapper.toResponse(
                attendanceRepository.save(attendance));
    }

    @Override
    @Transactional
    public EmployeeAttendanceResponse getAttendanceById(
            Long attendanceId) {

        EmployeeAttendance attendance = attendanceRepository
                .findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee attendance not found: " + attendanceId));

        return attendanceMapper.toResponse(attendance);
    }

    @Override
    public Page<EmployeeAttendanceResponse> getDailyAttendance(
            Long organizationId,
            LocalDate attendanceDate,
            Pageable pageable) {

        return attendanceRepository
                .findByOrganization_IdAndAttendanceDate(
                        organizationId, attendanceDate, pageable)
                .map(attendanceMapper::toResponse);
    }

    @Override
    public List<EmployeeAttendanceResponse> getAttendanceBetweenDates(
            Long organizationId,
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        return attendanceRepository
                .findByOrganization_IdAndAttendanceDateBetween(
                        organizationId, startDate, endDate)
                .stream()
                .map(attendanceMapper::toResponse)
                .toList();
    }

    @Override
    public List<EmployeeAttendanceResponse>
    getEmployeeAttendanceBetweenDates(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException(
                    "Employee not found: " + employeeId);
        }

        return attendanceRepository
                .findByEmployee_IdAndAttendanceDateBetween(
                        employeeId, startDate, endDate)
                .stream()
                .map(attendanceMapper::toResponse)
                .toList();
    }

    @Override
    public long countByStatus(
            Long organizationId,
            LocalDate attendanceDate,
            EmployeeAttendanceStatus status) {

        return attendanceRepository
                .countByOrganization_IdAndAttendanceDateAndStatus(
                        organizationId, attendanceDate, status);
    }

    private void validateRequest(EmployeeAttendanceRequest request) {

        if (request.getCheckInTime() != null
                && request.getCheckOutTime() != null
                && request.getCheckOutTime()
                .isBefore(request.getCheckInTime())) {

            throw new IllegalArgumentException(
                    "Check-out time cannot be earlier than check-in time");
        }
    }

    private void validateDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date");
        }
    }

    private void applyRequest(
            EmployeeAttendance attendance,
            EmployeeAttendanceRequest request) {

        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());
        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());
        attendance.setRemarks(request.getRemarks());
    }
    @Override
    @Transactional
    public List<EmployeeMonthlyAttendanceResponse> getMonthlyAttendanceSummary(
            Long organizationId,
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        List<Object[]> rows =
                attendanceRepository.getMonthlyAttendanceCounts(
                        organizationId, startDate, endDate);

        Map<Long, EmployeeMonthlyAttendanceResponse.EmployeeMonthlyAttendanceResponseBuilder>
                summaries = new java.util.LinkedHashMap<>();

        Map<Long, long[]> countsByEmployee = new java.util.LinkedHashMap<>();

        for (Object[] row : rows) {
            Long employeeId = (Long) row[0];
            EmployeeAttendanceStatus status =
                    (EmployeeAttendanceStatus) row[1];
            long count = ((Number) row[2]).longValue();

            long[] counts = countsByEmployee.computeIfAbsent(
                    employeeId, key -> new long[6]);

            switch (status) {
                case PRESENT -> counts[0] += count;
                case ABSENT -> counts[1] += count;
                case HALF_DAY -> counts[2] += count;
                case ON_LEAVE -> counts[3] += count;
                case HOLIDAY -> counts[4] += count;
                case WEEKLY_OFF -> counts[5] += count;
            }
        }

        List<EmployeeMonthlyAttendanceResponse> result =
                new java.util.ArrayList<>();

        for (Map.Entry<Long, long[]> entry : countsByEmployee.entrySet()) {
            Long employeeId = entry.getKey();
            long[] counts = entry.getValue();

            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found: " + employeeId));

            long recordedDays = java.util.Arrays.stream(counts).sum();

            // Half-day counts as half a present day.
            double attendancePercentage = recordedDays == 0 ? 0.0
                    : ((counts[0] + counts[2] * 0.5) / recordedDays) * 100.0;

            result.add(EmployeeMonthlyAttendanceResponse.builder()
                    .employeeId(employeeId)
                    .employeeCode(employee.getEmployeeCode())
                    .employeeName(employee.getFirstName() + " "
                            + employee.getLastName())
                    .presentDays(counts[0])
                    .absentDays(counts[1])
                    .halfDays(counts[2])
                    .leaveDays(counts[3])
                    .holidays(counts[4])
                    .weeklyOffs(counts[5])
                    .totalRecordedDays(recordedDays)
                    .attendancePercentage(
                            Math.round(attendancePercentage * 100.0) / 100.0)
                    .build());
        }
        return result;
    }

    @Override
    public EmployeeAttendanceResponse checkIn(
            Long employeeId,
            Long organizationId,
            LocalDate attendanceDate,
            java.time.LocalTime checkInTime) {

        if (attendanceDate == null || checkInTime == null) {
            throw new IllegalArgumentException(
                    "Attendance date and check-in time are required");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + employeeId));

        if (!employee.getOrganization().getId().equals(organizationId)) {
            throw new IllegalArgumentException(
                    "Employee does not belong to the specified organization");
        }

        if (!Boolean.TRUE.equals(employee.getActive())) {
            throw new IllegalArgumentException(
                    "Attendance cannot be marked for an inactive employee");
        }

        EmployeeAttendance attendance =
                attendanceRepository
                        .findByEmployee_IdAndAttendanceDate(
                                employeeId, attendanceDate)
                        .orElse(null);

        if (attendance != null && attendance.getCheckInTime() != null) {
            throw new DuplicateResourceException(
                    "Employee has already checked in for this date",attendance.getEmployee().getEmployeeCode());
        }

        if (attendance == null) {
            Organization organization = organizationRepository
                    .findById(organizationId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Organization not found: " + organizationId));

            attendance = new EmployeeAttendance();
            attendance.setEmployee(employee);
            attendance.setOrganization(organization);
            attendance.setAttendanceDate(attendanceDate);
            attendance.setStatus(EmployeeAttendanceStatus.PRESENT);
        }

        if (attendance.getCheckOutTime() != null
                && attendance.getCheckOutTime().isBefore(checkInTime)) {
            throw new IllegalArgumentException(
                    "Check-in time cannot be later than the existing check-out time");
        }

        attendance.setCheckInTime(checkInTime);

        return attendanceMapper.toResponse(
                attendanceRepository.save(attendance));
    }

    @Override
    public EmployeeAttendanceResponse checkOut(
            Long employeeId,
            Long organizationId,
            LocalDate attendanceDate,
            java.time.LocalTime checkOutTime) {

        if (attendanceDate == null || checkOutTime == null) {
            throw new IllegalArgumentException(
                    "Attendance date and check-out time are required");
        }

        EmployeeAttendance attendance = attendanceRepository
                .findByEmployee_IdAndAttendanceDate(
                        employeeId, attendanceDate)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attendance record not found. Check in first."));

        if (!attendance.getOrganization().getId().equals(organizationId)) {
            throw new IllegalArgumentException(
                    "Attendance does not belong to the specified organization");
        }

        if (attendance.getCheckInTime() == null) {
            throw new IllegalArgumentException(
                    "Employee must check in before checking out");
        }

        if (attendance.getCheckOutTime() != null) {
            throw new DuplicateResourceException(
                    "Employee has already checked out for this date", attendance.getEmployee().getEmployeeCode());
        }

        if (checkOutTime.isBefore(attendance.getCheckInTime())) {
            throw new IllegalArgumentException(
                    "Check-out time cannot be earlier than check-in time");
        }

        attendance.setCheckOutTime(checkOutTime);

        return attendanceMapper.toResponse(
                attendanceRepository.save(attendance));
    }

    @Override
    @Transactional
    public EmployeeAttendanceDashboardResponse getDashboard(
            Long organizationId,
            LocalDate attendanceDate) {

        if (organizationId == null || attendanceDate == null) {
            throw new IllegalArgumentException(
                    "Organization ID and attendance date are required");
        }

        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException(
                    "Organization not found: " + organizationId);
        }

        long totalActive = employeeRepository
                .countByOrganization_IdAndActiveTrue(organizationId);

        long present = attendanceRepository
                .countByOrganization_IdAndAttendanceDateAndStatus(
                        organizationId, attendanceDate,
                        EmployeeAttendanceStatus.PRESENT);

        long absent = attendanceRepository
                .countByOrganization_IdAndAttendanceDateAndStatus(
                        organizationId, attendanceDate,
                        EmployeeAttendanceStatus.ABSENT);

        long halfDay = attendanceRepository
                .countByOrganization_IdAndAttendanceDateAndStatus(
                        organizationId, attendanceDate,
                        EmployeeAttendanceStatus.HALF_DAY);

        long onLeave = attendanceRepository
                .countByOrganization_IdAndAttendanceDateAndStatus(
                        organizationId, attendanceDate,
                        EmployeeAttendanceStatus.ON_LEAVE);

        long holidays = attendanceRepository
                .countByOrganization_IdAndAttendanceDateAndStatus(
                        organizationId, attendanceDate,
                        EmployeeAttendanceStatus.HOLIDAY);

        long weeklyOff = attendanceRepository
                .countByOrganization_IdAndAttendanceDateAndStatus(
                        organizationId, attendanceDate,
                        EmployeeAttendanceStatus.WEEKLY_OFF);

        long recorded = present + absent + halfDay
                + onLeave + holidays + weeklyOff;
        long notMarked = Math.max(0, totalActive - recorded);
        // Exclude leave, holidays and weekly offs from the denominator.
        long workingDayRecords = present + absent + halfDay;
        double percentage = workingDayRecords == 0
                ? 0.0
                : ((present + halfDay * 0.5) / workingDayRecords) * 100.0;

        return EmployeeAttendanceDashboardResponse.builder()
                .organizationId(organizationId)
                .totalActiveEmployees(totalActive)
                .totalRecordedEmployees(recorded)
                .present(present)
                .absent(absent)
                .halfDay(halfDay)
                .onLeave(onLeave)
                .holidays(holidays)
                .weeklyOff(weeklyOff)
                .notMarked(notMarked)
                .attendancePercentage(
                        Math.round(percentage * 100.0) / 100.0)
                .build();
    }

    @Override
    @Transactional
    public BulkEmployeeAttendanceResponse markBulkAttendance(
            BulkEmployeeAttendanceRequest request) {

        if (request.getRecords() == null
                || request.getRecords().isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one attendance record is required");
        }

        if (request.getRecords().size() > 500) {
            throw new IllegalArgumentException(
                    "A maximum of 500 attendance records is allowed per request");
        }

        Long organizationId = request.getOrganizationId();
        LocalDate attendanceDate = request.getAttendanceDate();

        if (organizationId == null || attendanceDate == null) {
            throw new IllegalArgumentException(
                    "Organization ID and attendance date are required");
        }

        Organization organization = organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organization not found: " + organizationId));

        // Validate the complete batch before saving anything.
        java.util.Set<Long> employeeIds = new java.util.HashSet<>();

        for (EmployeeAttendanceRequest record : request.getRecords()) {

            validateRequest(record);

            if (!organizationId.equals(record.getOrganizationId())) {
                throw new IllegalArgumentException(
                        "Every record must use the batch organization ID");
            }

            if (!attendanceDate.equals(record.getAttendanceDate())) {
                throw new IllegalArgumentException(
                        "Every record must use the batch attendance date");
            }

            if (!employeeIds.add(record.getEmployeeId())) {
                throw new IllegalArgumentException(
                        "Duplicate employee ID in batch: "
                                + record.getEmployeeId());
            }

            Employee employee = employeeRepository
                    .findById(record.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found: " + record.getEmployeeId()));

            if (!organizationId.equals(
                    employee.getOrganization().getId())) {
                throw new IllegalArgumentException(
                        "Employee " + record.getEmployeeId()
                                + " does not belong to organization "
                                + organizationId);
            }

            if (!Boolean.TRUE.equals(employee.getActive())) {
                throw new IllegalArgumentException(
                        "Employee " + record.getEmployeeId()
                                + " is inactive");
            }

            if (attendanceRepository.existsByEmployee_IdAndAttendanceDate(
                    record.getEmployeeId(), attendanceDate)) {
                throw new DuplicateResourceException(
                        "Attendance already exists for employee "
                                , record.getEmployeeId()
                                + " on " + attendanceDate);
            }
        }

        List<EmployeeAttendance> entities = new java.util.ArrayList<>();

        for (EmployeeAttendanceRequest record : request.getRecords()) {

            Employee employee = employeeRepository
                    .findById(record.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found: " + record.getEmployeeId()));

            EmployeeAttendance attendance = new EmployeeAttendance();
            attendance.setEmployee(employee);
            attendance.setOrganization(organization);
            applyRequest(attendance, record);

            entities.add(attendance);
        }

        List<EmployeeAttendanceResponse> savedRecords =
                attendanceRepository.saveAll(entities)
                        .stream()
                        .map(attendanceMapper::toResponse)
                        .toList();

        return BulkEmployeeAttendanceResponse.builder()
                .totalRequested(request.getRecords().size())
                .totalSaved(savedRecords.size())
                .records(savedRecords)
                .build();
    }
}