package com.smartexam.examsubject.service.impl;

import com.smartexam.examsubject.dto.ExamSubjectRequest;
import com.smartexam.examsubject.dto.ExamSubjectResponse;
import com.smartexam.examsubject.dto.ExamSubjectSearchRequest;
import com.smartexam.examsubject.entity.ExamSubject;
import com.smartexam.examsubject.mapper.ExamSubjectMapper;
import com.smartexam.examsubject.repository.ExamSubjectRepository;
import com.smartexam.examsubject.service.ExamSubjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamSubjectServiceImpl
        implements ExamSubjectService {

    private final ExamSubjectRepository examSubjectRepository;

    private final ExamSubjectMapper examSubjectMapper;

    public ExamSubjectServiceImpl(
            ExamSubjectRepository examSubjectRepository,
            ExamSubjectMapper examSubjectMapper) {

        this.examSubjectRepository =
                examSubjectRepository;

        this.examSubjectMapper =
                examSubjectMapper;
    }

    @Override
    public ExamSubjectResponse create(
            ExamSubjectRequest request) {

        validateExamSubject(
                request,
                null
        );

        ExamSubject examSubject =
                examSubjectMapper.toEntity(request);

        ExamSubject savedExamSubject =
                examSubjectRepository.save(examSubject);

        return examSubjectMapper.toResponse(
                savedExamSubject
        );
    }

    @Override
    public ExamSubjectResponse update(
            Long id,
            ExamSubjectRequest request) {

        ExamSubject examSubject =
                findById(id);

        validateExamSubject(
                request,
                id
        );

        examSubjectMapper.updateEntity(
                request,
                examSubject
        );

        ExamSubject updatedExamSubject =
                examSubjectRepository.save(examSubject);

        return examSubjectMapper.toResponse(
                updatedExamSubject
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ExamSubjectResponse getById(
            Long id) {

        return examSubjectMapper.toResponse(
                findById(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getAll() {

        return examSubjectMapper.toResponseList(
                examSubjectRepository.findByActiveTrue()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getByExamId(
            Long examId) {

        return examSubjectMapper.toResponseList(
                examSubjectRepository
                        .findByExamIdAndActiveTrue(examId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getBySubjectId(
            Long subjectId) {

        return examSubjectMapper.toResponseList(
                examSubjectRepository
                        .findBySubjectIdAndActiveTrue(subjectId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSubjectResponse> getByOrganizationId(
            Long organizationId) {

        return examSubjectMapper.toResponseList(
                examSubjectRepository
                        .findByOrganizationIdAndActiveTrue(
                                organizationId
                        )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamSubjectResponse> getPage(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return examSubjectRepository
                .findByActiveTrue(pageable)
                .map(examSubjectMapper::toResponse);
    }

    @Override
    public ExamSubjectResponse activate(
            Long id) {

        ExamSubject examSubject =
                findById(id);

        examSubject.setActive(true);

        ExamSubject updatedExamSubject =
                examSubjectRepository.save(examSubject);

        return examSubjectMapper.toResponse(
                updatedExamSubject
        );
    }

    @Override
    public ExamSubjectResponse deactivate(
            Long id) {

        ExamSubject examSubject =
                findById(id);

        examSubject.setActive(false);

        ExamSubject updatedExamSubject =
                examSubjectRepository.save(examSubject);

        return examSubjectMapper.toResponse(
                updatedExamSubject
        );
    }

    @Override
    public void delete(Long id) {

        ExamSubject examSubject =
                findById(id);

        examSubjectRepository.delete(examSubject);
    }

    /**
     * Find ExamSubject by ID.
     */
    private ExamSubject findById(
            Long id) {

        return examSubjectRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Exam subject not found with id : "
                                        + id
                        )
                );
    }

    /**
     * Main business validations.
     */
    private void validateExamSubject(
            ExamSubjectRequest request,
            Long id) {

        validateDuplicate(
                request,
                id
        );

        validateMarks(request);

        validateExamTime(request);

        /*
         * Cross-module validations will be added
         * using your existing repositories/services:
         *
         * 1. Organization exists
         * 2. Exam exists
         * 3. Subject exists
         * 4. Exam belongs to Organization
         * 5. Subject belongs to Organization
         * 6. Exam date must be between
         *    Exam.startDate and Exam.endDate
         */
    }

    /**
     * Prevent duplicate subject assignment
     * for the same exam.
     */
    private void validateDuplicate(
            ExamSubjectRequest request,
            Long id) {

        boolean exists;

        if (id == null) {

            exists = examSubjectRepository
                    .existsByExamIdAndSubjectId(
                            request.getExamId(),
                            request.getSubjectId()
                    );

        } else {

            exists = examSubjectRepository
                    .existsByExamIdAndSubjectIdAndIdNot(
                            request.getExamId(),
                            request.getSubjectId(),
                            id
                    );
        }

        if (exists) {

            throw new IllegalArgumentException(
                    "This subject is already assigned "
                            + "to the selected exam."
            );
        }
    }

    /**
     * Passing marks cannot exceed maximum marks.
     */
    private void validateMarks(
            ExamSubjectRequest request) {

        if (request.getPassingMarks()
                .compareTo(
                        request.getMaximumMarks()
                ) > 0) {

            throw new IllegalArgumentException(
                    "Passing marks cannot be greater "
                            + "than maximum marks."
            );
        }
    }

    /**
     * End time must be after start time.
     */
    private void validateExamTime(
            ExamSubjectRequest request) {

        if (request.getStartTime() != null
                && request.getEndTime() != null
                && !request.getEndTime()
                .isAfter(
                        request.getStartTime()
                )) {

            throw new IllegalArgumentException(
                    "End time must be after start time."
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamSubjectResponse> search(
            ExamSubjectSearchRequest request,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Specification<ExamSubject> specification =
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

        if (request.getSubjectId() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("subjectId"),
                                    request.getSubjectId()
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

        if (request.getActive() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("active"),
                                    request.getActive()
                            )
            );
        }

        return examSubjectRepository
                .findAll(specification, pageable)
                .map(examSubjectMapper::toResponse);
    }
}