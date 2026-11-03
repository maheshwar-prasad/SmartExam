package com.smartexam.timetable.service.impl;

import com.smartexam.common.exception.BusinessException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.teacherSubjectAssignment.entity.TeacherSubjectAssignment;
import com.smartexam.teacherSubjectAssignment.repository.TeacherSubjectAssignmentRepository;
import com.smartexam.timetable.dto.TimetableRequest;
import com.smartexam.timetable.dto.TimetableResponse;
import com.smartexam.timetable.dto.TimetableSearchRequest;
import com.smartexam.timetable.dto.TimetableUpdateRequest;
import com.smartexam.timetable.entity.Timetable;
import com.smartexam.timetable.mapper.TimetableMapper;
import com.smartexam.timetable.repository.TimetableRepository;
import com.smartexam.timetable.service.TimetableService;
import com.smartexam.timetable.specification.TimetableSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TimetableServiceImpl implements TimetableService {

    private final TimetableRepository repository;
    private final TimetableMapper mapper;
    private final TeacherSubjectAssignmentRepository
            teacherSubjectAssignmentRepository;

    @Override
    public TimetableResponse create(TimetableRequest request) {

        log.info(
                "Creating timetable for assignment {} on {}",
                request.getTeacherSubjectAssignmentId(),
                request.getDayOfWeek());

        TeacherSubjectAssignment assignment =
                getTeacherSubjectAssignment(
                        request.getTeacherSubjectAssignmentId());

        validateAssignmentActive(assignment);

        validateTimeRange(
                request.getStartTime(),
                request.getEndTime());

        validateCreateConflicts(
                assignment,
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime());

        Timetable timetable = mapper.toEntity(request);

        timetable.setTeacherSubjectAssignment(assignment);
        timetable.setActive(true);

        Timetable saved = repository.save(timetable);

        log.info(
                "Timetable created successfully with id {}",
                saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public TimetableResponse update(
            Long id,
            TimetableUpdateRequest request) {

        log.info("Updating timetable {}", id);

        Timetable timetable = getTimetable(id);

        TeacherSubjectAssignment assignment =
                request.getTeacherSubjectAssignmentId() != null
                        ? getTeacherSubjectAssignment(
                        request.getTeacherSubjectAssignmentId())
                        : timetable.getTeacherSubjectAssignment();

        validateAssignmentActive(assignment);

        var dayOfWeek =
                request.getDayOfWeek() != null
                        ? request.getDayOfWeek()
                        : timetable.getDayOfWeek();

        LocalTime startTime =
                request.getStartTime() != null
                        ? request.getStartTime()
                        : timetable.getStartTime();

        LocalTime endTime =
                request.getEndTime() != null
                        ? request.getEndTime()
                        : timetable.getEndTime();

        String roomNumber =
                request.getRoomNumber() != null
                        ? request.getRoomNumber()
                        : timetable.getRoomNumber();

        Boolean active =
                request.getActive() != null
                        ? request.getActive()
                        : timetable.getActive();

        validateTimeRange(startTime, endTime);

        /*
         * Only validate conflicts when the timetable
         * will remain active.
         */
        if (Boolean.TRUE.equals(active)) {
            validateUpdateConflicts(
                    id,
                    assignment,
                    dayOfWeek,
                    startTime,
                    endTime);
        }

        timetable.setTeacherSubjectAssignment(assignment);
        timetable.setDayOfWeek(dayOfWeek);
        timetable.setStartTime(startTime);
        timetable.setEndTime(endTime);
        timetable.setRoomNumber(roomNumber);
        timetable.setActive(active);

        Timetable updated = repository.save(timetable);

        log.info(
                "Timetable updated successfully with id {}",
                updated.getId());

        return mapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public TimetableResponse getById(Long id) {

        return mapper.toResponse(getTimetable(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimetableResponse> getAll() {

        return mapper.toResponseList(
                repository.findByActiveTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TimetableResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = createSort(sortBy, direction);

        return repository
                .findAll(PageRequest.of(page, size, sort))
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TimetableResponse> search(
            TimetableSearchRequest request) {

        Sort sort = createSort(
                request.getSortBy(),
                request.getDirection());

        PageRequest pageRequest =
                PageRequest.of(
                        request.getPage(),
                        request.getSize(),
                        sort);

        return repository
                .findAll(
                        TimetableSpecification.withFilters(request),
                        pageRequest)
                .map(mapper::toResponse);
    }

    @Override
    public TimetableResponse activate(Long id) {

        Timetable timetable = getTimetable(id);

        if (Boolean.TRUE.equals(timetable.getActive())) {
            return mapper.toResponse(timetable);
        }

        validateAssignmentActive(
                timetable.getTeacherSubjectAssignment());

        validateUpdateConflicts(
                timetable.getId(),
                timetable.getTeacherSubjectAssignment(),
                timetable.getDayOfWeek(),
                timetable.getStartTime(),
                timetable.getEndTime());

        timetable.setActive(true);

        return mapper.toResponse(repository.save(timetable));
    }

    @Override
    public TimetableResponse deactivate(Long id) {

        Timetable timetable = getTimetable(id);

        if (Boolean.FALSE.equals(timetable.getActive())) {
            return mapper.toResponse(timetable);
        }

        timetable.setActive(false);

        return mapper.toResponse(repository.save(timetable));
    }

    @Override
    public void delete(Long id) {

        Timetable timetable = getTimetable(id);

        repository.delete(timetable);

        log.info(
                "Timetable deleted successfully with id {}",
                id);
    }

    /*
     * =====================================================
     * PRIVATE HELPER METHODS
     * =====================================================
     */

    private Timetable getTimetable(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Timetable",
                                id));
    }

    private TeacherSubjectAssignment getTeacherSubjectAssignment(
            Long id) {

        return teacherSubjectAssignmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Teacher Subject Assignment",
                                id));
    }

    private void validateAssignmentActive(
            TeacherSubjectAssignment assignment) {

        if (!Boolean.TRUE.equals(assignment.getActive())) {
            throw new BusinessException(
                    "Teacher Subject Assignment is inactive.");
        }
    }

    private void validateTimeRange(
            LocalTime startTime,
            LocalTime endTime) {

        if (startTime == null || endTime == null) {
            throw new BusinessException(
                    "Start time and End time are required.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new BusinessException(
                    "Start time must be before End time.");
        }
    }

    private void validateCreateConflicts(
            TeacherSubjectAssignment assignment,
            com.smartexam.common.enums.DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime) {

        boolean teacherConflict =
                repository.existsTeacherTimeConflict(
                        assignment.getTeacher().getId(),
                        dayOfWeek,
                        startTime,
                        endTime);

        if (teacherConflict) {
            throw new BusinessException(
                    "Teacher already has another class during this time.");
        }

        boolean classConflict =
                repository.existsClassSectionTimeConflict(
                        assignment.getClassRoom().getId(),
                        assignment.getSection().getId(),
                        dayOfWeek,
                        startTime,
                        endTime);

        if (classConflict) {
            throw new BusinessException(
                    "Class and Section already have another class during this time.");
        }
    }

    private void validateUpdateConflicts(
            Long timetableId,
            TeacherSubjectAssignment assignment,
            com.smartexam.common.enums.DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime) {

        boolean teacherConflict =
                repository.existsTeacherTimeConflictExcludingId(
                        timetableId,
                        assignment.getTeacher().getId(),
                        dayOfWeek,
                        startTime,
                        endTime);

        if (teacherConflict) {
            throw new BusinessException(
                    "Teacher already has another class during this time.");
        }

        boolean classConflict =
                repository.existsClassSectionTimeConflictExcludingId(
                        timetableId,
                        assignment.getClassRoom().getId(),
                        assignment.getSection().getId(),
                        dayOfWeek,
                        startTime,
                        endTime);

        if (classConflict) {
            throw new BusinessException(
                    "Class and Section already have another class during this time.");
        }
    }

    private Sort createSort(
            String sortBy,
            String direction) {

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "dayOfWeek";
        }

        Sort.Direction sortDirection =
                "DESC".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return Sort.by(sortDirection, sortBy);
    }
}