package com.smartexam.examregistration.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.exam.entity.Exam;
import com.smartexam.exam.repository.ExamRepository;
import com.smartexam.examregistration.dto.StudentExamRegistrationRequest;
import com.smartexam.examregistration.dto.StudentExamRegistrationResponse;
import com.smartexam.examregistration.entity.StudentExamRegistration;
import com.smartexam.examregistration.mapper.StudentExamRegistrationMapper;
import com.smartexam.examregistration.repository.StudentExamRegistrationRepository;
import com.smartexam.examregistration.service.StudentExamRegistrationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import com.smartexam.examregistration.dto.StudentExamRegistrationSearchRequest;
@Service
@Transactional
public class StudentExamRegistrationServiceImpl
        implements StudentExamRegistrationService {

    private final StudentExamRegistrationRepository registrationRepository;

    private final StudentExamRegistrationMapper registrationMapper;

    private final ExamRepository examRepository;

    public StudentExamRegistrationServiceImpl(
            StudentExamRegistrationRepository registrationRepository,
            StudentExamRegistrationMapper registrationMapper,
            ExamRepository examRepository) {

        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.examRepository = examRepository;
    }

    /**
     * Register student for exam.
     */
    @Override
    public StudentExamRegistrationResponse create(
            StudentExamRegistrationRequest request) {

        validateExam(request.getExamId());

        validateDuplicateStudentExam(
                request.getStudentId(),
                request.getExamId()
        );

        StudentExamRegistration registration =
                registrationMapper.toEntity(request);

        if (registration.getRegistrationNumber() == null
                || registration.getRegistrationNumber().isBlank()) {

            registration.setRegistrationNumber(
                    generateRegistrationNumber(
                            request.getOrganizationId()
                    )
            );
        }

        validateRegistrationNumber(
                registration.getRegistrationNumber()
        );

        if (registration.getRegistrationDate() == null) {
            registration.setRegistrationDate(
                    LocalDate.now()
            );
        }

        registration.setStatus("REGISTERED");
        registration.setActive(true);

        StudentExamRegistration saved =
                registrationRepository.save(registration);

        return registrationMapper.toResponse(saved);
    }

    /**
     * Update student exam registration.
     */
    @Override
    public StudentExamRegistrationResponse update(
            Long id,
            StudentExamRegistrationRequest request) {

        StudentExamRegistration existing =
                getRegistrationEntity(id);

        validateExam(request.getExamId());

        validateDuplicateStudentExamExcludingId(
                request.getStudentId(),
                request.getExamId(),
                id
        );

        if (request.getRegistrationNumber() != null
                && !request.getRegistrationNumber().isBlank()) {

            validateRegistrationNumberExcludingId(
                    request.getRegistrationNumber(),
                    id
            );
        }

        registrationMapper.updateEntity(
                request,
                existing
        );

        StudentExamRegistration updated =
                registrationRepository.save(existing);

        return registrationMapper.toResponse(updated);
    }

    /**
     * Get registration by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public StudentExamRegistrationResponse getById(
            Long id) {

        return registrationMapper.toResponse(
                getRegistrationEntity(id)
        );
    }

    /**
     * Get all active registrations.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentExamRegistrationResponse> getAll() {

        return registrationMapper.toResponseList(
                registrationRepository.findByActiveTrue()
        );
    }

    /**
     * Get registrations by student ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentExamRegistrationResponse> getByStudentId(
            Long studentId) {

        return registrationMapper.toResponseList(
                registrationRepository
                        .findByStudentIdAndActiveTrue(studentId)
        );
    }

    /**
     * Get registrations by exam ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentExamRegistrationResponse> getByExamId(
            Long examId) {

        return registrationMapper.toResponseList(
                registrationRepository
                        .findByExamIdAndActiveTrue(examId)
        );
    }

    /**
     * Get registrations by organization ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentExamRegistrationResponse> getByOrganizationId(
            Long organizationId) {

        return registrationMapper.toResponseList(
                registrationRepository
                        .findByOrganizationIdAndActiveTrue(
                                organizationId
                        )
        );
    }

    /**
     * Get paginated active registrations.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentExamRegistrationResponse> getPage(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return registrationRepository
                .findByActiveTrue(pageable)
                .map(registrationMapper::toResponse);
    }

    /**
     * Cancel registration.
     */
    @Override
    public StudentExamRegistrationResponse cancel(
            Long id) {

        StudentExamRegistration registration =
                getRegistrationEntity(id);

        registration.setStatus("CANCELLED");

        StudentExamRegistration updated =
                registrationRepository.save(registration);

        return registrationMapper.toResponse(updated);
    }

    /**
     * Activate registration.
     */
    @Override
    public StudentExamRegistrationResponse activate(
            Long id) {

        StudentExamRegistration registration =
                getRegistrationEntity(id);

        registration.setActive(true);

        StudentExamRegistration updated =
                registrationRepository.save(registration);

        return registrationMapper.toResponse(updated);
    }

    /**
     * Deactivate registration.
     */
    @Override
    public StudentExamRegistrationResponse deactivate(
            Long id) {

        StudentExamRegistration registration =
                getRegistrationEntity(id);

        registration.setActive(false);

        StudentExamRegistration updated =
                registrationRepository.save(registration);

        return registrationMapper.toResponse(updated);
    }

    /**
     * Delete registration.
     */
    @Override
    public void delete(Long id) {

        StudentExamRegistration registration =
                getRegistrationEntity(id);

        registrationRepository.delete(registration);
    }

    /**
     * Get registration entity.
     */
    private StudentExamRegistration getRegistrationEntity(
            Long id) {

        return registrationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student exam registration not found with id: "
                                        + id
                        )
                );
    }

    /**
     * Validate exam exists.
     */
    private void validateExam(Long examId) {

        Exam exam = examRepository
                .findById(examId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Exam not found with id: "
                                        + examId
                        )
                );

        if (Boolean.FALSE.equals(exam.getActive())) {

            throw new IllegalArgumentException(
                    "Cannot register student for an inactive exam"
            );
        }
    }

    /**
     * Validate duplicate Student + Exam.
     */
    private void validateDuplicateStudentExam(
            Long studentId,
            Long examId) {

        boolean exists =
                registrationRepository
                        .existsByStudentIdAndExamId(
                                studentId,
                                examId
                        );

        if (exists) {
            throw new IllegalArgumentException(
                    "Student is already registered for this exam"
            );
        }
    }

    /**
     * Validate duplicate during update.
     */
    private void validateDuplicateStudentExamExcludingId(
            Long studentId,
            Long examId,
            Long id) {

        boolean exists =
                registrationRepository
                        .existsByStudentIdAndExamIdAndIdNot(
                                studentId,
                                examId,
                                id
                        );

        if (exists) {
            throw new IllegalArgumentException(
                    "Student is already registered for this exam"
            );
        }
    }

    /**
     * Validate registration number.
     */
    private void validateRegistrationNumber(
            String registrationNumber) {

        if (registrationRepository
                .existsByRegistrationNumber(registrationNumber)) {

            throw new IllegalArgumentException(
                    "Registration number already exists: "
                            + registrationNumber
            );
        }
    }

    /**
     * Validate registration number during update.
     */
    private void validateRegistrationNumberExcludingId(
            String registrationNumber,
            Long id) {

        if (registrationRepository
                .existsByRegistrationNumberAndIdNot(
                        registrationNumber,
                        id
                )) {

            throw new IllegalArgumentException(
                    "Registration number already exists: "
                            + registrationNumber
            );
        }
    }

    /**
     * Generate registration number.
     *
     * Example:
     * REG-1-20260823-001
     */
    private String generateRegistrationNumber(
            Long organizationId) {

        String registrationNumber;

        do {
            long count = registrationRepository.count() + 1;

            registrationNumber =
                    "REG-"
                            + organizationId
                            + "-"
                            + System.currentTimeMillis()
                            + "-"
                            + count;

        } while (registrationRepository
                .existsByRegistrationNumber(
                        registrationNumber
                ));

        return registrationNumber;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentExamRegistrationResponse> search(
            StudentExamRegistrationSearchRequest request,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Specification<StudentExamRegistration> specification =
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

        if (request.getRegistrationNumber() != null
                && !request.getRegistrationNumber().isBlank()) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(
                                    criteriaBuilder.upper(
                                            root.get("registrationNumber")
                                    ),
                                    "%" + request
                                            .getRegistrationNumber()
                                            .trim()
                                            .toUpperCase()
                                            + "%"
                            )
            );
        }

        if (request.getStatus() != null
                && !request.getStatus().isBlank()) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    criteriaBuilder.upper(
                                            root.get("status")
                                    ),
                                    request.getStatus()
                                            .trim()
                                            .toUpperCase()
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

        return registrationRepository
                .findAll(specification, pageable)
                .map(registrationMapper::toResponse);
    }

}