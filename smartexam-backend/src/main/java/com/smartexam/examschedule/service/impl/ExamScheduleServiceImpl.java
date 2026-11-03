package com.smartexam.examschedule.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.examschedule.dto.ExamScheduleRequest;
import com.smartexam.examschedule.dto.ExamScheduleResponse;
import com.smartexam.examschedule.dto.ExamScheduleSearchRequest;
import com.smartexam.examschedule.entity.ExamSchedule;
import com.smartexam.examschedule.mapper.ExamScheduleMapper;
import com.smartexam.examschedule.repository.ExamScheduleRepository;
import com.smartexam.examschedule.service.ExamScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamScheduleServiceImpl
        implements ExamScheduleService {

    private final ExamScheduleRepository
            examScheduleRepository;

    private final ExamScheduleMapper
            examScheduleMapper;

    public ExamScheduleServiceImpl(
            ExamScheduleRepository examScheduleRepository,
            ExamScheduleMapper examScheduleMapper) {

        this.examScheduleRepository =
                examScheduleRepository;

        this.examScheduleMapper =
                examScheduleMapper;
    }

    /**
     * Create Exam Schedule.
     */
    @Override
    public ExamScheduleResponse create(
            ExamScheduleRequest request) {

        validateTime(
                request.getStartTime(),
                request.getEndTime()
        );

        validateDuplicate(
                request.getExamId(),
                request.getExamSubjectId()
        );

        ExamSchedule examSchedule =
                examScheduleMapper.toEntity(request);

        examSchedule.setActive(true);

        ExamSchedule savedExamSchedule =
                examScheduleRepository.save(examSchedule);

        return examScheduleMapper.toResponse(
                savedExamSchedule
        );
    }

    /**
     * Update Exam Schedule.
     */
    @Override
    public ExamScheduleResponse update(
            Long id,
            ExamScheduleRequest request) {

        ExamSchedule existingExamSchedule =
                getExamScheduleEntity(id);

        validateTime(
                request.getStartTime(),
                request.getEndTime()
        );

        validateDuplicateExcludingId(
                request.getExamId(),
                request.getExamSubjectId(),
                id
        );

        examScheduleMapper.updateEntity(
                request,
                existingExamSchedule
        );

        ExamSchedule updatedExamSchedule =
                examScheduleRepository.save(
                        existingExamSchedule
                );

        return examScheduleMapper.toResponse(
                updatedExamSchedule
        );
    }

    /**
     * Get Exam Schedule by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ExamScheduleResponse getById(
            Long id) {

        return examScheduleMapper.toResponse(
                getExamScheduleEntity(id)
        );
    }

    /**
     * Get all active Exam Schedules.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getAll() {

        List<ExamSchedule> examSchedules =
                examScheduleRepository.findByActiveTrue();

        return examScheduleMapper.toResponseList(
                examSchedules
        );
    }

    /**
     * Get Exam Schedules by Exam ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getByExamId(
            Long examId) {

        return examScheduleMapper.toResponseList(
                examScheduleRepository
                        .findByExamIdAndActiveTrue(examId)
        );
    }

    /**
     * Get Exam Schedules by Exam Subject ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse>
    getByExamSubjectId(Long examSubjectId) {

        return examScheduleMapper.toResponseList(
                examScheduleRepository
                        .findByExamSubjectIdAndActiveTrue(
                                examSubjectId
                        )
        );
    }

    /**
     * Get Exam Schedules by Organization ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExamScheduleResponse>
    getByOrganizationId(Long organizationId) {

        return examScheduleMapper.toResponseList(
                examScheduleRepository
                        .findByOrganizationIdAndActiveTrue(
                                organizationId
                        )
        );
    }

    /**
     * Get paginated active Exam Schedules.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExamScheduleResponse> getPage(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return examScheduleRepository
                .findByActiveTrue(pageable)
                .map(examScheduleMapper::toResponse);
    }

    /**
     * Activate Exam Schedule.
     */
    @Override
    public ExamScheduleResponse activate(Long id) {

        ExamSchedule examSchedule =
                getExamScheduleEntity(id);

        examSchedule.setActive(true);

        ExamSchedule updatedExamSchedule =
                examScheduleRepository.save(
                        examSchedule
                );

        return examScheduleMapper.toResponse(
                updatedExamSchedule
        );
    }

    /**
     * Deactivate Exam Schedule.
     */
    @Override
    public ExamScheduleResponse deactivate(Long id) {

        ExamSchedule examSchedule =
                getExamScheduleEntity(id);

        examSchedule.setActive(false);

        ExamSchedule updatedExamSchedule =
                examScheduleRepository.save(
                        examSchedule
                );

        return examScheduleMapper.toResponse(
                updatedExamSchedule
        );
    }

    /**
     * Delete Exam Schedule.
     */
    @Override
    public void delete(Long id) {

        ExamSchedule examSchedule =
                getExamScheduleEntity(id);

        examScheduleRepository.delete(
                examSchedule
        );
    }

    /**
     * Find Exam Schedule entity.
     */
    private ExamSchedule getExamScheduleEntity(
            Long id) {

        return examScheduleRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Exam Schedule not found with id: "
                                        + id
                        )
                );
    }

    /**
     * Validate Start and End Time.
     */
    private void validateTime(
            java.time.LocalTime startTime,
            java.time.LocalTime endTime) {

        if (!endTime.isAfter(startTime)) {

            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }
    }

    /**
     * Validate duplicate Exam Schedule during create.
     */
    private void validateDuplicate(
            Long examId,
            Long examSubjectId) {

        boolean exists =
                examScheduleRepository
                        .existsByExamIdAndExamSubjectId(
                                examId,
                                examSubjectId
                        );

        if (exists) {

            throw new IllegalArgumentException(
                    "Exam Schedule already exists for "
                            + "this Exam and Exam Subject"
            );
        }
    }

    /**
     * Validate duplicate Exam Schedule during update.
     */
    private void validateDuplicateExcludingId(
            Long examId,
            Long examSubjectId,
            Long id) {

        boolean exists =
                examScheduleRepository
                        .existsByExamIdAndExamSubjectIdAndIdNot(
                                examId,
                                examSubjectId,
                                id
                        );

        if (exists) {

            throw new IllegalArgumentException(
                    "Exam Schedule already exists for "
                            + "this Exam and Exam Subject"
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamScheduleResponse> search(
            ExamScheduleSearchRequest request,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Specification<ExamSchedule> specification =
                (root, query, criteriaBuilder) -> null;

        if (request.getOrganizationId() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("organizationId"),
                                    request.getOrganizationId()
                            )
            );
        }

        if (request.getExamId() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("examId"),
                                    request.getExamId()
                            )
            );
        }

        if (request.getExamSubjectId() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("examSubjectId"),
                                    request.getExamSubjectId()
                            )
            );
        }

        if (request.getExamDate() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("examDate"),
                                    request.getExamDate()
                            )
            );
        }

        if (request.getRoomId() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("roomId"),
                                    request.getRoomId()
                            )
            );
        }

        if (request.getActive() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("active"),
                                    request.getActive()
                            )
            );
        }

        return examScheduleRepository
                .findAll(specification, pageable)
                .map(examScheduleMapper::toResponse);
    }
}