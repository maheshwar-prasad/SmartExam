package com.smartexam.studentwithdrawal.service.impl;

import com.smartexam.common.enums.EnrollmentStatus;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.student.entity.Student;
import com.smartexam.student.repository.StudentRepository;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import com.smartexam.studentenrollment.repository.StudentEnrollmentRepository;
import com.smartexam.studentwithdrawal.dto.StudentWithdrawalRequest;
import com.smartexam.studentwithdrawal.dto.StudentWithdrawalResponse;
import com.smartexam.studentwithdrawal.entity.StudentWithdrawal;
import com.smartexam.studentwithdrawal.enums.WithdrawalStatus;
import com.smartexam.studentwithdrawal.repository.StudentWithdrawalRepository;
import com.smartexam.studentwithdrawal.service.StudentWithdrawalService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentWithdrawalServiceImpl
        implements StudentWithdrawalService {

    private final StudentWithdrawalRepository withdrawalRepository;

    private final StudentRepository studentRepository;

    private final StudentEnrollmentRepository enrollmentRepository;

    private final CurrentUserService currentUserService;


    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public StudentWithdrawalResponse create(
            StudentWithdrawalRequest request
    ) {

        Long organizationId =
                currentUserService.getOrganizationId();

        // -------------------------------------------------
        // Find active student
        // -------------------------------------------------

        Student student =
                studentRepository
                        .findByIdAndActiveTrue(request.getStudentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student",
                                        request.getStudentId().toString()
                                )
                        );

        // -------------------------------------------------
        // Validate organization
        // -------------------------------------------------

        if (
                student.getOrganization() == null ||
                        !organizationId.equals(
                                student.getOrganization().getId()
                        )
        ) {

            throw new ResourceNotFoundException(
                    "Student",
                    request.getStudentId().toString()
            );
        }

        // -------------------------------------------------
        // Prevent duplicate withdrawal
        // -------------------------------------------------

        if (
                withdrawalRepository
                        .existsByStudentIdAndOrganizationId(
                                request.getStudentId(),
                                organizationId
                        )
        ) {

            throw new DuplicateResourceException(
                    "Student Withdrawal",
                    request.getStudentId().toString()
            );
        }

        // -------------------------------------------------
        // Find active enrollment
        // -------------------------------------------------

        StudentEnrollment enrollment =
                enrollmentRepository
                        .findFirstByStudentIdAndActiveTrueOrderByIdDesc(
                                request.getStudentId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active student enrollment",
                                        request.getStudentId().toString()
                                )
                        );

        // -------------------------------------------------
        // Validate enrollment belongs to organization
        // -------------------------------------------------

        if (
                enrollment.getStudent() == null ||
                        enrollment.getStudent().getOrganization() == null ||
                        !organizationId.equals(
                                enrollment
                                        .getStudent()
                                        .getOrganization()
                                        .getId()
                        )
        ) {

            throw new ResourceNotFoundException(
                    "Student enrollment",
                    request.getStudentId().toString()
            );
        }

        // -------------------------------------------------
        // Create withdrawal record
        // -------------------------------------------------

        StudentWithdrawal withdrawal =
                StudentWithdrawal.builder()
                        .student(student)
                        .organizationId(organizationId)
                        .withdrawalDate(
                                request.getWithdrawalDate()
                        )
                        .reason(
                                request.getReason().trim()
                        )
                        .remarks(
                                request.getRemarks()
                        )
                        .lastClassName(
                                request.getLastClassName()
                        )
                        .lastAcademicSession(
                                request.getLastAcademicSession()
                        )
                        .status(
                                WithdrawalStatus.ACTIVE
                        )
                        .build();

        StudentWithdrawal saved =
                withdrawalRepository.save(withdrawal);

        // -------------------------------------------------
        // Change enrollment status
        // -------------------------------------------------

        enrollment.setEnrollmentStatus(
                EnrollmentStatus.DROPPED
        );

        enrollment.setActive(false);

        enrollmentRepository.save(enrollment);

        return toResponse(saved);
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public StudentWithdrawalResponse update(
            Long id,
            StudentWithdrawalRequest request
    ) {

        Long organizationId =
                currentUserService.getOrganizationId();

        StudentWithdrawal withdrawal =
                withdrawalRepository
                        .findByIdAndOrganizationId(
                                id,
                                organizationId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student Withdrawal",
                                        id.toString()
                                )
                        );

        // -------------------------------------------------
        // Do not allow changing student during update
        // -------------------------------------------------

        if (
                withdrawal.getStudent() == null ||
                        !request.getStudentId().equals(
                                withdrawal.getStudent().getId()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Student cannot be changed for an existing withdrawal record."
            );
        }

        withdrawal.setWithdrawalDate(
                request.getWithdrawalDate()
        );

        withdrawal.setReason(
                request.getReason().trim()
        );

        withdrawal.setRemarks(
                request.getRemarks()
        );

        withdrawal.setLastClassName(
                request.getLastClassName()
        );

        withdrawal.setLastAcademicSession(
                request.getLastAcademicSession()
        );

        return toResponse(
                withdrawalRepository.save(withdrawal)
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public StudentWithdrawalResponse getById(
            Long id
    ) {

        Long organizationId =
                currentUserService.getOrganizationId();

        StudentWithdrawal withdrawal =
                withdrawalRepository
                        .findByIdAndOrganizationId(
                                id,
                                organizationId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student Withdrawal",
                                        id.toString()
                                )
                        );

        return toResponse(withdrawal);
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<StudentWithdrawalResponse> getAll(
            Pageable pageable
    ) {

        Long organizationId =
                currentUserService.getOrganizationId();

        return withdrawalRepository
                .findByOrganizationIdAndActiveTrue(
                        organizationId,
                        pageable
                )
                .map(this::toResponse);
    }


    // =====================================================
    // ACTIVATE
    // =====================================================

    @Override
    public void activate(Long id) {

        Long organizationId =
                currentUserService.getOrganizationId();

        StudentWithdrawal withdrawal =
                withdrawalRepository
                        .findByIdAndOrganizationId(
                                id,
                                organizationId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student Withdrawal",
                                        id.toString()
                                )
                        );

        withdrawal.setActive(true);

        withdrawal.setStatus(
                WithdrawalStatus.ACTIVE
        );

        withdrawalRepository.save(withdrawal);
    }


    // =====================================================
    // DEACTIVATE
    // =====================================================

    @Override
    public void deactivate(Long id) {

        Long organizationId =
                currentUserService.getOrganizationId();

        StudentWithdrawal withdrawal =
                withdrawalRepository
                        .findByIdAndOrganizationId(
                                id,
                                organizationId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student Withdrawal",
                                        id.toString()
                                )
                        );

        withdrawal.setActive(false);

        withdrawal.setStatus(
                WithdrawalStatus.INACTIVE
        );

        withdrawalRepository.save(withdrawal);
    }


    // =====================================================
    // RESPONSE MAPPER
    // =====================================================

    private StudentWithdrawalResponse toResponse(
            StudentWithdrawal withdrawal
    ) {

        Student student =
                withdrawal.getStudent();

        String studentName = null;

        if (student != null) {

            studentName =
                    (
                            student.getFirstName() +
                                    " " +
                                    (
                                            student.getLastName() == null
                                                    ? ""
                                                    : student.getLastName()
                                    )
                    ).trim();
        }

        return StudentWithdrawalResponse.builder()

                .id(withdrawal.getId())
                .studentId(
                        student != null
                                ? student.getId()
                                : null
                )
                .studentCode(
                        student != null
                                ? student.getStudentCode()
                                : null
                )
                .admissionNo(
                        student != null
                                ? student.getAdmissionNo()
                                : null
                )
                .studentName(studentName)

                .mobile(
                        student != null
                                ? student.getMobile()
                                : null
                )
                .email(
                        student != null
                                ? student.getEmail()
                                : null
                )
                .organizationId(
                        withdrawal.getOrganizationId()
                )
                .withdrawalDate(
                        withdrawal.getWithdrawalDate()
                )
                .reason(
                        withdrawal.getReason()
                )
                .remarks(
                        withdrawal.getRemarks()
                )
                .lastClassName(
                        withdrawal.getLastClassName()
                )
                .lastAcademicSession(
                        withdrawal.getLastAcademicSession()
                )
                .status(
                        withdrawal.getStatus()
                )
                .active(
                        withdrawal.getActive()
                )
                .createdAt(
                        withdrawal.getCreatedAt() != null
                                ? withdrawal
                                .getCreatedAt()
                                .toString()
                                : null
                )
                .updatedAt(
                        withdrawal.getUpdatedAt() != null
                                ? withdrawal
                                .getUpdatedAt()
                                .toString()
                                : null
                )
                .build();
    }
}