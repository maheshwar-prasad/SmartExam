package com.smartexam.attendance.service.impl;

import com.smartexam.attendance.dto.*;
import com.smartexam.attendance.entity.Attendance;
import com.smartexam.attendance.mapper.AttendanceMapper;
import com.smartexam.attendance.repository.AttendanceRepository;
import com.smartexam.attendance.service.AttendanceService;
import com.smartexam.attendance.specification.AttendanceSpecification;
import com.smartexam.common.enums.AttendanceStatus;
import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import com.smartexam.studentenrollment.repository.StudentEnrollmentRepository;
import com.smartexam.teacherSubjectAssignment.entity.TeacherSubjectAssignment;
import com.smartexam.teacherSubjectAssignment.repository.TeacherSubjectAssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repository;
    private final AttendanceMapper mapper;

    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final TeacherSubjectAssignmentRepository teacherSubjectAssignmentRepository;

    @Override
    public AttendanceResponse create(
            AttendanceRequest request) {

        log.info("Creating Attendance for StudentEnrollment={} Date={}",
                request.getStudentEnrollmentId(),
                request.getAttendanceDate());

        if (repository.existsByAttendanceDateAndStudentEnrollmentIdAndTeacherSubjectAssignmentId(
                request.getAttendanceDate(),
                request.getStudentEnrollmentId(),
                request.getTeacherSubjectAssignmentId())) {

            throw new DuplicateResourceException(
                    "Attendance",
                    "Attendance already marked.");
        }

        StudentEnrollment studentEnrollment =
                studentEnrollmentRepository.findById(request.getStudentEnrollmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student Enrollment",
                                        request.getStudentEnrollmentId()));

        TeacherSubjectAssignment assignment =
                teacherSubjectAssignmentRepository.findById(request.getTeacherSubjectAssignmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher Subject Assignment",
                                        request.getTeacherSubjectAssignmentId()));

        if (!Boolean.TRUE.equals(studentEnrollment.getActive())) {
            throw new BusinessException(
                    "Student Enrollment is inactive.");
        }

        if (!Boolean.TRUE.equals(assignment.getActive())) {
            throw new BusinessException(
                    "Teacher Subject Assignment is inactive.");
        }

        if (!studentEnrollment.getAcademicSession().getId()
                .equals(assignment.getAcademicSession().getId())) {

            throw new BusinessException(
                    "Student Enrollment and Teacher Subject Assignment belong to different Academic Sessions.");
        }

        if (!studentEnrollment.getClassRoom().getId()
                .equals(assignment.getClassRoom().getId())) {

            throw new BusinessException(
                    "Student Enrollment and Teacher Subject Assignment belong to different Class Rooms.");
        }

        if (!studentEnrollment.getSection().getId()
                .equals(assignment.getSection().getId())) {

            throw new BusinessException(
                    "Student Enrollment and Teacher Subject Assignment belong to different Sections.");
        }

        Attendance attendance = mapper.toEntity(request);

        attendance.setStudentEnrollment(studentEnrollment);
        attendance.setTeacherSubjectAssignment(assignment);
        attendance.setActive(true);

        Attendance saved = repository.save(attendance);

        log.info("Attendance Created : {}", saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public AttendanceResponse update(
            Long id,
            AttendanceUpdateRequest request) {

        log.info("Updating Attendance : {}", id);

        Attendance attendance = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance",
                                id));

        StudentEnrollment studentEnrollment =
                attendance.getStudentEnrollment();

        TeacherSubjectAssignment assignment =
                attendance.getTeacherSubjectAssignment();

        // Update Student Enrollment
        if (request.getStudentEnrollmentId() != null) {

            studentEnrollment =
                    studentEnrollmentRepository.findById(
                                    request.getStudentEnrollmentId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Student Enrollment",
                                            request.getStudentEnrollmentId()));
        }

        // Update Teacher Subject Assignment
        if (request.getTeacherSubjectAssignmentId() != null) {

            assignment =
                    teacherSubjectAssignmentRepository.findById(
                                    request.getTeacherSubjectAssignmentId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Teacher Subject Assignment",
                                            request.getTeacherSubjectAssignmentId()));
        }

        LocalDate attendanceDate =
                request.getAttendanceDate() != null
                        ? request.getAttendanceDate()
                        : attendance.getAttendanceDate();

        // Duplicate Validation
        if (repository.existsByAttendanceDateAndStudentEnrollmentIdAndTeacherSubjectAssignmentIdAndIdNot(
                attendanceDate,
                studentEnrollment.getId(),
                assignment.getId(),
                id)) {

            throw new DuplicateResourceException(
                    "Attendance",
                    "Attendance already marked.");
        }

        // Common Validation
        validateEnrollmentAndAssignment(
                studentEnrollment,
                assignment);

        // Partial Update
        mapper.updateEntity(request, attendance);

        attendance.setStudentEnrollment(studentEnrollment);
        attendance.setTeacherSubjectAssignment(assignment);

        Attendance updated = repository.save(attendance);

        log.info("Attendance Updated : {}", updated.getId());

        return mapper.toResponse(updated);
    }

    private void validateEnrollmentAndAssignment(
            StudentEnrollment enrollment,
            TeacherSubjectAssignment assignment) {

        if (!Boolean.TRUE.equals(enrollment.getActive())) {

            throw new BusinessException(
                    "Student Enrollment is inactive.");
        }

        if (!Boolean.TRUE.equals(assignment.getActive())) {

            throw new BusinessException(
                    "Teacher Subject Assignment is inactive.");
        }

        if (!enrollment.getAcademicSession().getId()
                .equals(assignment.getAcademicSession().getId())) {

            throw new BusinessException(
                    "Student Enrollment and Teacher Subject Assignment belong to different Academic Sessions.");
        }

        if (!enrollment.getClassRoom().getId()
                .equals(assignment.getClassRoom().getId())) {

            throw new BusinessException(
                    "Student Enrollment and Teacher Subject Assignment belong to different Class Rooms.");
        }

        if (!enrollment.getSection().getId()
                .equals(assignment.getSection().getId())) {

            throw new BusinessException(
                    "Student Enrollment and Teacher Subject Assignment belong to different Sections.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceResponse getById(Long id) {

        log.info("Fetching Attendance : {}", id);

        Attendance attendance = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance",
                                id));

        return mapper.toResponse(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAll() {

        log.info("Fetching all Attendance");

        return mapper.toResponseList(
                repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        log.info("Fetching Attendance with pagination");

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceResponse> search(
            AttendanceSearchRequest request) {

        log.info("Searching Attendance");

        Specification<Attendance> specification =
                AttendanceSpecification.hasKeyword(request.getKeyword())
                        .and(AttendanceSpecification.hasStudentEnrollment(request.getStudentEnrollmentId()))
                        .and(AttendanceSpecification.hasTeacherSubjectAssignment(request.getTeacherSubjectAssignmentId()))
                        .and(AttendanceSpecification.hasStudent(request.getStudentId()))
                        .and(AttendanceSpecification.hasTeacher(request.getTeacherId()))
                        .and(AttendanceSpecification.hasSubject(request.getSubjectId()))
                        .and(AttendanceSpecification.hasAcademicSession(request.getAcademicSessionId()))
                        .and(AttendanceSpecification.hasClassRoom(request.getClassRoomId()))
                        .and(AttendanceSpecification.hasSection(request.getSectionId()))
                        .and(AttendanceSpecification.hasStatus(request.getStatus()))
                        .and(AttendanceSpecification.hasAttendanceDate(request.getAttendanceDate()))
                        .and(AttendanceSpecification.betweenDates(
                                request.getFromDate(),
                                request.getToDate()))
                        .and(AttendanceSpecification.isActive(request.getActive()));

        Sort sort = request.getDirection().equalsIgnoreCase("DESC")
                ? Sort.by(request.getSortBy()).descending()
                : Sort.by(request.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort);

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public AttendanceResponse activate(Long id) {

        log.info("Activating Attendance : {}", id);

        Attendance attendance = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance",
                                id));

        attendance.setActive(true);

        Attendance updated = repository.save(attendance);

        return mapper.toResponse(updated);
    }

    @Override
    public AttendanceResponse deactivate(Long id) {

        log.info("Deactivating Attendance : {}", id);

        Attendance attendance = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance",
                                id));

        attendance.setActive(false);

        Attendance updated = repository.save(attendance);

        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        log.info("Deleting Attendance : {}", id);

        Attendance attendance = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance",
                                id));

        attendance.setActive(false);

        repository.save(attendance);
    }

    @Override
    public List<AttendanceResponse> markBulkAttendance(BulkAttendanceRequest request) {

        log.info(
                "Marking bulk attendance. Assignment={} Date={} Students={}",
                request.getTeacherSubjectAssignmentId(),
                request.getAttendanceDate(),
                request.getStudents().size());

        TeacherSubjectAssignment assignment =
                teacherSubjectAssignmentRepository
                        .findById(request.getTeacherSubjectAssignmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher Subject Assignment",
                                        request.getTeacherSubjectAssignmentId()));

        if (!Boolean.TRUE.equals(assignment.getActive())) {
            throw new BusinessException(
                    "Teacher Subject Assignment is inactive.");
        }

        validateDuplicateStudentEnrollments(request);

        List<Attendance> attendances = new ArrayList<>();

        for (BulkAttendanceStudentRequest studentRequest
                : request.getStudents()) {

            StudentEnrollment enrollment =
                    studentEnrollmentRepository
                            .findById(studentRequest.getStudentEnrollmentId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Student Enrollment",
                                            studentRequest.getStudentEnrollmentId()));

            validateEnrollmentAndAssignment(
                    enrollment,
                    assignment);

            boolean alreadyMarked =
                    repository
                            .existsByAttendanceDateAndStudentEnrollmentIdAndTeacherSubjectAssignmentId(
                                    request.getAttendanceDate(),
                                    enrollment.getId(),
                                    assignment.getId());

            if (alreadyMarked) {
                throw new DuplicateResourceException(
                        "Attendance",
                        "Attendance already marked for Student Enrollment ID: "
                                + enrollment.getId());
            }

            Attendance attendance = Attendance.builder()
                    .attendanceDate(request.getAttendanceDate())
                    .studentEnrollment(enrollment)
                    .teacherSubjectAssignment(assignment)
                    .status(studentRequest.getStatus())
                    .remarks(studentRequest.getRemarks())
                    .active(true)
                    .build();

            attendances.add(attendance);
        }

        List<Attendance> savedAttendances =
                repository.saveAll(attendances);

        log.info(
                "Bulk attendance successfully created. Count={}",
                savedAttendances.size());

        return mapper.toResponseList(savedAttendances);
    }

    private void validateDuplicateStudentEnrollments(
            BulkAttendanceRequest request) {

        Set<Long> studentEnrollmentIds = new HashSet<>();

        for (BulkAttendanceStudentRequest student
                : request.getStudents()) {

            if (!studentEnrollmentIds.add(
                    student.getStudentEnrollmentId())) {

                throw new BusinessException(
                        "Duplicate Student Enrollment ID found in request: "
                                + student.getStudentEnrollmentId());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getDailyAttendance(
            LocalDate attendanceDate,
            Long classRoomId,
            Long sectionId) {

        log.info(
                "Fetching daily attendance. Date={}, ClassRoom={}, Section={}",
                attendanceDate,
                classRoomId,
                sectionId);

        List<Attendance> attendances =
                repository
                        .findByAttendanceDateAndStudentEnrollmentClassRoomIdAndStudentEnrollmentSectionIdAndActiveTrue(
                                attendanceDate,
                                classRoomId,
                                sectionId);

        return mapper.toResponseList(attendances);
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyAttendanceSummaryResponse getMonthlyAttendanceSummary(
            Long studentEnrollmentId,
            int year,
            int month) {

        log.info(
                "Fetching monthly attendance summary. StudentEnrollment={}, Year={}, Month={}",
                studentEnrollmentId,
                year,
                month);

        StudentEnrollment enrollment =
                studentEnrollmentRepository.findById(studentEnrollmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student Enrollment",
                                        studentEnrollmentId));

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate fromDate = yearMonth.atDay(1);
        LocalDate toDate = yearMonth.atEndOfMonth();

        List<Attendance> attendances =
                repository
                        .findByStudentEnrollmentIdAndAttendanceDateBetweenAndActiveTrue(
                                studentEnrollmentId,
                                fromDate,
                                toDate);

        long totalDays = attendances.size();

        long presentDays = countByStatus(
                attendances,
                AttendanceStatus.PRESENT);

        long absentDays = countByStatus(
                attendances,
                AttendanceStatus.ABSENT);

        long lateDays = countByStatus(
                attendances,
                AttendanceStatus.LATE);

        long halfDays = countByStatus(
                attendances,
                AttendanceStatus.HALF_DAY);

        long leaveDays = countByStatus(
                attendances,
                AttendanceStatus.LEAVE);

        double attendancePercentage =
                calculateAttendancePercentage(
                        totalDays,
                        presentDays,
                        lateDays,
                        halfDays);

        return MonthlyAttendanceSummaryResponse.builder()
                .studentEnrollmentId(enrollment.getId())
                .year(year)
                .month(month)
                .totalDays(totalDays)
                .presentDays(presentDays)
                .absentDays(absentDays)
                .lateDays(lateDays)
                .halfDays(halfDays)
                .leaveDays(leaveDays)
                .attendancePercentage(attendancePercentage)
                .build();
    }

    private long countByStatus(
            List<Attendance> attendances,
            AttendanceStatus status) {

        return attendances.stream()
                .filter(attendance ->
                        attendance.getStatus() == status)
                .count();
    }

    private double calculateAttendancePercentage(
            long totalDays,
            long presentDays,
            long lateDays,
            long halfDays) {

        if (totalDays == 0) {
            return 0.0;
        }
        double attendedDays =
                presentDays
                        + lateDays
                        + (halfDays * 0.5);

        return Math.round(
                (attendedDays * 100.0 / totalDays) * 100.0
        ) / 100.0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getTeacherAttendance(
            Long teacherId) {

        log.info(
                "Fetching attendance for Teacher={}",
                teacherId);

        repository.findById(teacherId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher",
                                teacherId));

        List<Attendance> attendances =
                repository
                        .findByTeacherSubjectAssignmentTeacherIdAndActiveTrue(
                                teacherId);

        return mapper.toResponseList(attendances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getTeacherAttendance(
            Long teacherId,
            LocalDate fromDate,
            LocalDate toDate) {

        log.info(
                "Fetching attendance for Teacher={} from={} to={}",
                teacherId,
                fromDate,
                toDate);

        repository.findById(teacherId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher",
                                teacherId));

        validateDateRange(fromDate, toDate);

        List<Attendance> attendances =
                repository
                        .findByTeacherSubjectAssignmentTeacherIdAndAttendanceDateBetweenAndActiveTrue(
                                teacherId,
                                fromDate,
                                toDate);

        return mapper.toResponseList(attendances);
    }
    private void validateDateRange(
            LocalDate fromDate,
            LocalDate toDate) {

        if (fromDate == null || toDate == null) {
            throw new BusinessException(
                    "From date and To date are required.");
        }

        if (fromDate.isAfter(toDate)) {
            throw new BusinessException(
                    "From date cannot be after To date.");
        }
    }

}