package com.smartexam.marks.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.examsubject.entity.ExamSubject;
import com.smartexam.examsubject.repository.ExamSubjectRepository;
import com.smartexam.marks.dto.StudentMarksRequest;
import com.smartexam.marks.dto.StudentMarksResponse;
import com.smartexam.marks.dto.StudentMarksSearchRequest;
import com.smartexam.marks.entity.StudentMarks;
import com.smartexam.marks.mapper.StudentMarksMapper;
import com.smartexam.marks.repository.StudentMarksRepository;
import com.smartexam.marks.service.StudentMarksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class StudentMarksServiceImpl
        implements StudentMarksService {

    private final StudentMarksRepository studentMarksRepository;

    private final StudentMarksMapper studentMarksMapper;

    private final ExamSubjectRepository examSubjectRepository;

    public StudentMarksServiceImpl(
            StudentMarksRepository studentMarksRepository,
            StudentMarksMapper studentMarksMapper,
            ExamSubjectRepository examSubjectRepository) {

        this.studentMarksRepository =
                studentMarksRepository;

        this.studentMarksMapper =
                studentMarksMapper;

        this.examSubjectRepository =
                examSubjectRepository;
    }

    /**
     * Create Student Marks.
     */
    @Override
    public StudentMarksResponse create(
            StudentMarksRequest request) {

        validateDuplicate(
                request.getStudentId(),
                request.getExamSubjectId()
        );

        ExamSubject examSubject =
                getExamSubject(
                        request.getExamSubjectId()
                );

        validateExamRelationship(
                request.getExamId(),
                examSubject
        );

        BigDecimal maximumMarks =
                examSubject.getMaximumMarks();

        validateObtainedMarks(
                request.getObtainedMarks(),
                maximumMarks
        );

        StudentMarks studentMarks =
                studentMarksMapper.toEntity(request);

        studentMarks.setMaximumMarks(
                maximumMarks
        );

        studentMarks.setGrade(
                calculateGrade(
                        request.getObtainedMarks(),
                        maximumMarks
                )
        );

        studentMarks.setPublished(false);

        studentMarks.setActive(true);

        StudentMarks savedStudentMarks =
                studentMarksRepository.save(
                        studentMarks
                );

        return studentMarksMapper.toResponse(
                savedStudentMarks
        );
    }

    /**
     * Update Student Marks.
     */
    @Override
    public StudentMarksResponse update(
            Long id,
            StudentMarksRequest request) {

        StudentMarks existingStudentMarks =
                getStudentMarksEntity(id);

        validateDuplicateExcludingId(
                request.getStudentId(),
                request.getExamSubjectId(),
                id
        );

        ExamSubject examSubject =
                getExamSubject(
                        request.getExamSubjectId()
                );

        validateExamRelationship(
                request.getExamId(),
                examSubject
        );

        BigDecimal maximumMarks =
                examSubject.getMaximumMarks();

        validateObtainedMarks(
                request.getObtainedMarks(),
                maximumMarks
        );

        studentMarksMapper.updateEntity(
                request,
                existingStudentMarks
        );

        existingStudentMarks.setMaximumMarks(
                maximumMarks
        );

        existingStudentMarks.setGrade(
                calculateGrade(
                        request.getObtainedMarks(),
                        maximumMarks
                )
        );

        StudentMarks updatedStudentMarks =
                studentMarksRepository.save(
                        existingStudentMarks
                );

        return studentMarksMapper.toResponse(
                updatedStudentMarks
        );
    }

    /**
     * Get Student Marks by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public StudentMarksResponse getById(
            Long id) {

        return studentMarksMapper.toResponse(
                getStudentMarksEntity(id)
        );
    }

    /**
     * Get all active Student Marks.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentMarksResponse> getAll() {

        return studentMarksMapper.toResponseList(
                studentMarksRepository.findByActiveTrue()
        );
    }

    /**
     * Get Student Marks by Student ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentMarksResponse> getByStudentId(
            Long studentId) {

        return studentMarksMapper.toResponseList(
                studentMarksRepository
                        .findByStudentIdAndActiveTrue(studentId)
        );
    }

    /**
     * Get Student Marks by Exam ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentMarksResponse> getByExamId(
            Long examId) {

        return studentMarksMapper.toResponseList(
                studentMarksRepository
                        .findByExamIdAndActiveTrue(examId)
        );
    }

    /**
     * Get Student Marks by Exam Subject ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentMarksResponse> getByExamSubjectId(
            Long examSubjectId) {

        return studentMarksMapper.toResponseList(
                studentMarksRepository
                        .findByExamSubjectIdAndActiveTrue(
                                examSubjectId
                        )
        );
    }

    /**
     * Get Student Marks by Organization ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentMarksResponse> getByOrganizationId(
            Long organizationId) {

        return studentMarksMapper.toResponseList(
                studentMarksRepository
                        .findByOrganizationIdAndActiveTrue(
                                organizationId
                        )
        );
    }

    /**
     * Get paginated active Student Marks.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentMarksResponse> getPage(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return studentMarksRepository
                .findByActiveTrue(pageable)
                .map(studentMarksMapper::toResponse);
    }

    /**
     * Publish Student Marks.
     */
    @Override
    public StudentMarksResponse publish(
            Long id) {

        StudentMarks studentMarks =
                getStudentMarksEntity(id);

        studentMarks.setPublished(true);

        StudentMarks updatedStudentMarks =
                studentMarksRepository.save(
                        studentMarks
                );

        return studentMarksMapper.toResponse(
                updatedStudentMarks
        );
    }

    /**
     * Unpublish Student Marks.
     */
    @Override
    public StudentMarksResponse unpublish(
            Long id) {

        StudentMarks studentMarks =
                getStudentMarksEntity(id);

        studentMarks.setPublished(false);

        StudentMarks updatedStudentMarks =
                studentMarksRepository.save(
                        studentMarks
                );

        return studentMarksMapper.toResponse(
                updatedStudentMarks
        );
    }

    /**
     * Activate Student Marks.
     */
    @Override
    public StudentMarksResponse activate(
            Long id) {

        StudentMarks studentMarks =
                getStudentMarksEntity(id);

        studentMarks.setActive(true);

        StudentMarks updatedStudentMarks =
                studentMarksRepository.save(
                        studentMarks
                );

        return studentMarksMapper.toResponse(
                updatedStudentMarks
        );
    }

    /**
     * Deactivate Student Marks.
     */
    @Override
    public StudentMarksResponse deactivate(
            Long id) {

        StudentMarks studentMarks =
                getStudentMarksEntity(id);

        studentMarks.setActive(false);

        StudentMarks updatedStudentMarks =
                studentMarksRepository.save(
                        studentMarks
                );

        return studentMarksMapper.toResponse(
                updatedStudentMarks
        );
    }

    /**
     * Delete Student Marks.
     */
    @Override
    public void delete(Long id) {

        StudentMarks studentMarks =
                getStudentMarksEntity(id);

        studentMarksRepository.delete(
                studentMarks
        );
    }

    private StudentMarks getStudentMarksEntity(
            Long id) {

        return studentMarksRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student Marks not found with id: "
                                        + id
                        )
                );
    }

    private ExamSubject getExamSubject(
            Long examSubjectId) {

        return examSubjectRepository
                .findById(examSubjectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Exam Subject not found with id: "
                                        + examSubjectId
                        )
                );
    }

    /**
     * Validate that the ExamSubject belongs
     * to the requested Exam.
     */
    private void validateExamRelationship(
            Long examId,
            ExamSubject examSubject) {

        if (!examSubject.getExamId().equals(examId)) {

            throw new IllegalArgumentException(
                    "Exam Subject does not belong to the provided Exam"
            );
        }
    }

    /**
     * Obtained marks must not be greater
     * than maximum marks.
     */
    private void validateObtainedMarks(
            BigDecimal obtainedMarks,
            BigDecimal maximumMarks) {

        if (obtainedMarks.compareTo(maximumMarks) > 0) {

            throw new IllegalArgumentException(
                    "Obtained marks cannot be greater than maximum marks"
            );
        }
    }

    /**
     * Duplicate validation during create.
     */
    private void validateDuplicate(
            Long studentId,
            Long examSubjectId) {

        boolean exists =
                studentMarksRepository
                        .existsByStudentIdAndExamSubjectId(
                                studentId,
                                examSubjectId
                        );

        if (exists) {

            throw new IllegalArgumentException(
                    "Marks already exist for this student and exam subject"
            );
        }
    }

    /**
     * Duplicate validation during update.
     */
    private void validateDuplicateExcludingId(
            Long studentId,
            Long examSubjectId,
            Long id) {

        boolean exists =
                studentMarksRepository
                        .existsByStudentIdAndExamSubjectIdAndIdNot(
                                studentId,
                                examSubjectId,
                                id
                        );

        if (exists) {

            throw new IllegalArgumentException(
                    "Marks already exist for this student and exam subject"
            );
        }
    }

    /**
     * Calculate grade based on percentage.
     */
    private String calculateGrade(
            BigDecimal obtainedMarks,
            BigDecimal maximumMarks) {

        if (maximumMarks == null
                || maximumMarks.compareTo(BigDecimal.ZERO) == 0) {

            return null;
        }

        BigDecimal percentage =
                obtainedMarks
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                maximumMarks,
                                2,
                                java.math.RoundingMode.HALF_UP
                        );

        if (percentage.compareTo(
                BigDecimal.valueOf(90)) >= 0) {
            return "A+";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(80)) >= 0) {
            return "A";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(70)) >= 0) {
            return "B+";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(60)) >= 0) {
            return "B";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(50)) >= 0) {
            return "C";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(40)) >= 0) {
            return "D";
        }

        return "F";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentMarksResponse> search(
            StudentMarksSearchRequest request,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Specification<StudentMarks> specification =
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

        if (request.getStudentId() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("studentId"),
                                    request.getStudentId()
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

        if (request.getGrade() != null
                && !request.getGrade().isBlank()) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    criteriaBuilder.upper(
                                            root.get("grade")
                                    ),
                                    request.getGrade()
                                            .toUpperCase()
                            )
            );
        }

        if (request.getPublished() != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("published"),
                                    request.getPublished()
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

        return studentMarksRepository
                .findAll(specification, pageable)
                .map(studentMarksMapper::toResponse);
    }
}