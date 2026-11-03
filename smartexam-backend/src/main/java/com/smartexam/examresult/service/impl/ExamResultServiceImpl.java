package com.smartexam.examresult.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.exam.entity.Exam;
import com.smartexam.exam.repository.ExamRepository;
import com.smartexam.examregistration.repository.StudentExamRegistrationRepository;
import com.smartexam.examresult.dto.ExamResultRequest;
import com.smartexam.examresult.dto.ExamResultResponse;
import com.smartexam.examresult.dto.ExamResultSearchRequest;
import com.smartexam.examresult.entity.ExamResult;
import com.smartexam.examresult.mapper.ExamResultMapper;
import com.smartexam.examresult.repository.ExamResultRepository;
import com.smartexam.examresult.service.ExamResultService;
import com.smartexam.marks.entity.StudentMarks;
import com.smartexam.marks.repository.StudentMarksRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class ExamResultServiceImpl
        implements ExamResultService {

    private final ExamResultRepository examResultRepository;
    private final ExamResultMapper examResultMapper;
    private final ExamRepository examRepository;
    private final StudentExamRegistrationRepository registrationRepository;
    private final StudentMarksRepository studentMarksRepository;

    public ExamResultServiceImpl(
            ExamResultRepository examResultRepository,
            ExamResultMapper examResultMapper,
            ExamRepository examRepository,
            StudentExamRegistrationRepository registrationRepository,
            StudentMarksRepository studentMarksRepository) {

        this.examResultRepository = examResultRepository;
        this.examResultMapper = examResultMapper;
        this.examRepository = examRepository;
        this.registrationRepository = registrationRepository;
        this.studentMarksRepository = studentMarksRepository;
    }

    /**
     * Generate result for student and exam.
     */
    @Override
    public ExamResultResponse generate(
            ExamResultRequest request) {

        validateExam(request.getExamId());

        validateStudentRegistration(
                request.getStudentId(),
                request.getExamId()
        );

        validateDuplicateResult(
                request.getStudentId(),
                request.getExamId()
        );

        List<StudentMarks> studentMarks =
                studentMarksRepository
                        .findByStudentIdAndExamIdAndActiveTrue(
                                request.getStudentId(),
                                request.getExamId()
                        );

        if (studentMarks.isEmpty()) {

            throw new IllegalArgumentException(
                    "No marks found for this student and exam"
            );
        }

        int totalSubjects = studentMarks.size();

        BigDecimal totalMaximumMarks =
                studentMarks.stream()
                        .map(StudentMarks::getMaximumMarks)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal totalObtainedMarks =
                studentMarks.stream()
                        .map(StudentMarks::getObtainedMarks)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal percentage =
                calculatePercentage(
                        totalObtainedMarks,
                        totalMaximumMarks
                );

        String grade =
                determineGrade(percentage);

        String resultStatus =
                determineResultStatus(studentMarks);

        ExamResult examResult =
                examResultMapper.toEntity(request);

        examResult.setTotalSubjects(totalSubjects);
        examResult.setTotalMaximumMarks(totalMaximumMarks);
        examResult.setTotalObtainedMarks(totalObtainedMarks);
        examResult.setPercentage(percentage);
        examResult.setGrade(grade);
        examResult.setResultStatus(resultStatus);
        examResult.setPublished(false);
        examResult.setActive(true);

        ExamResult saved =
                examResultRepository.save(examResult);

        return examResultMapper.toResponse(saved);
    }

    /**
     * Get result by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ExamResultResponse getById(Long id) {

        return examResultMapper.toResponse(
                getExamResultEntity(id)
        );
    }

    /**
     * Get all active results.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getAll() {

        return examResultMapper.toResponseList(
                examResultRepository.findAll()
                        .stream()
                        .filter(result ->
                                Boolean.TRUE.equals(
                                        result.getActive()
                                )
                        )
                        .toList()
        );
    }

    /**
     * Get results by student.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getByStudentId(
            Long studentId) {

        return examResultMapper.toResponseList(
                examResultRepository
                        .findByStudentIdAndActiveTrue(studentId)
        );
    }

    /**
     * Get results by exam.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getByExamId(
            Long examId) {

        return examResultMapper.toResponseList(
                examResultRepository
                        .findByExamIdAndActiveTrue(examId)
        );
    }

    /**
     * Get result by student and exam.
     */
    @Override
    @Transactional(readOnly = true)
    public ExamResultResponse getByStudentAndExam(
            Long studentId,
            Long examId) {

        ExamResult result =
                examResultRepository
                        .findByStudentIdAndExamId(
                                studentId,
                                examId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exam result not found"
                                )
                        );

        return examResultMapper.toResponse(result);
    }

    /**
     * Get results by organization.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExamResultResponse> getByOrganizationId(
            Long organizationId) {

        return examResultMapper.toResponseList(
                examResultRepository
                        .findByOrganizationIdAndActiveTrue(
                                organizationId
                        )
        );
    }

    /**
     * Get paginated active results.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExamResultResponse> getPage(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return examResultRepository
                .findByActiveTrue(pageable)
                .map(examResultMapper::toResponse);
    }

    /**
     * Publish result.
     */
    @Override
    public ExamResultResponse publish(Long id) {

        ExamResult result =
                getExamResultEntity(id);

        result.setPublished(true);

        return examResultMapper.toResponse(
                examResultRepository.save(result)
        );
    }

    /**
     * Unpublish result.
     */
    @Override
    public ExamResultResponse unpublish(Long id) {

        ExamResult result =
                getExamResultEntity(id);

        result.setPublished(false);

        return examResultMapper.toResponse(
                examResultRepository.save(result)
        );
    }

    /**
     * Delete result.
     */
    @Override
    public void delete(Long id) {

        ExamResult result =
                getExamResultEntity(id);

        examResultRepository.delete(result);
    }

    /* =========================================================
       PRIVATE METHODS
       ========================================================= */

    private ExamResult getExamResultEntity(Long id) {

        return examResultRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Exam result not found with id: " + id
                        )
                );
    }

    private void validateExam(Long examId) {

        Exam exam =
                examRepository.findById(examId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exam not found with id: " + examId
                                )
                        );

        if (Boolean.FALSE.equals(exam.getActive())) {

            throw new IllegalArgumentException(
                    "Cannot generate result for inactive exam"
            );
        }
    }

    private void validateStudentRegistration(
            Long studentId,
            Long examId) {

        boolean registered =
                registrationRepository
                        .existsByStudentIdAndExamIdAndStatusAndActiveTrue(
                                studentId,
                                examId,
                                "REGISTERED"
                        );

        if (!registered) {

            throw new IllegalArgumentException(
                    "Student is not registered for this exam"
            );
        }
    }

    private void validateDuplicateResult(
            Long studentId,
            Long examId) {

        if (examResultRepository
                .existsByStudentIdAndExamId(
                        studentId,
                        examId
                )) {

            throw new IllegalArgumentException(
                    "Result already exists for this student and exam"
            );
        }
    }

    private BigDecimal calculatePercentage(
            BigDecimal obtainedMarks,
            BigDecimal maximumMarks) {

        if (maximumMarks == null
                || maximumMarks.compareTo(BigDecimal.ZERO) == 0) {

            return BigDecimal.ZERO;
        }

        return obtainedMarks
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        maximumMarks,
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private String determineGrade(
            BigDecimal percentage) {

        if (percentage.compareTo(
                BigDecimal.valueOf(90)
        ) >= 0) {
            return "A+";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(80)
        ) >= 0) {
            return "A";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(70)
        ) >= 0) {
            return "B+";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(60)
        ) >= 0) {
            return "B";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(50)
        ) >= 0) {
            return "C";
        }

        if (percentage.compareTo(
                BigDecimal.valueOf(40)
        ) >= 0) {
            return "D";
        }

        return "F";
    }

    private String determineResultStatus(
            List<StudentMarks> studentMarks) {

        if (studentMarks == null
                || studentMarks.isEmpty()) {

            return "INCOMPLETE";
        }

        boolean incomplete = studentMarks.stream()
                .anyMatch(mark ->
                        mark.getObtainedMarks() == null
                );

        if (incomplete) {
            return "INCOMPLETE";
        }

        boolean failedSubject = studentMarks.stream()
                .anyMatch(mark ->
                        mark.getPassMarks() != null
                                && mark.getObtainedMarks()
                                .compareTo(
                                        mark.getPassMarks()
                                ) < 0
                );

        if (failedSubject) {
            return "FAIL";
        }

        return "PASS";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamResultResponse> search(
            ExamResultSearchRequest request,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Specification<ExamResult> specification =
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

        if (request.getResultStatus() != null
                && !request.getResultStatus().isBlank()) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    criteriaBuilder.upper(
                                            root.get("resultStatus")
                                    ),
                                    request.getResultStatus()
                                            .trim()
                                            .toUpperCase()
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
                                            .trim()
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

        return examResultRepository
                .findAll(specification, pageable)
                .map(examResultMapper::toResponse);
    }
}