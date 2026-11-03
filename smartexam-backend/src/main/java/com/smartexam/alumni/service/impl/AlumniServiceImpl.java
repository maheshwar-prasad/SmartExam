package com.smartexam.alumni.service.impl;

import com.smartexam.alumni.dto.AlumniRequest;
import com.smartexam.alumni.dto.AlumniResponse;
import com.smartexam.alumni.entity.Alumni;
import com.smartexam.alumni.enums.AlumniStatus;
import com.smartexam.alumni.repository.AlumniRepository;
import com.smartexam.alumni.service.AlumniService;
import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.student.entity.Student;
import com.smartexam.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlumniServiceImpl implements AlumniService {

    private final AlumniRepository alumniRepository;
    private final StudentRepository studentRepository;
    private final CurrentUserService currentUserService;

    @Override
    public AlumniResponse create(
            AlumniRequest request
    ) {

        Long organizationId =
                currentUserService.getOrganizationId();

        if (alumniRepository
                .existsByStudentIdAndOrganizationId(
                        request.getStudentId(),
                        organizationId
                )) {

            throw new DuplicateResourceException(
                    "Student",
                    request.getStudentId().toString()
            );
        }

        Student student =
                studentRepository
                        .findByIdAndActiveTrue(
                                request.getStudentId()
                        )
                        .filter(
                                s -> organizationId.equals(
                                        s.getOrganization().getId()
                                )
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Student not found: "
                                                + request.getStudentId()
                                )
                        );

        Alumni alumni =
                Alumni.builder()
                        .student(student)
                        .organizationId(organizationId)
                        .leavingDate(request.getLeavingDate())
                        .graduationYear(request.getGraduationYear())
                        .lastClassName(request.getLastClassName())
                        .lastAcademicSession(
                                request.getLastAcademicSession()
                        )
                        .higherEducation(
                                request.getHigherEducation()
                        )
                        .currentOrganization(
                                request.getCurrentOrganization()
                        )
                        .currentDesignation(
                                request.getCurrentDesignation()
                        )
                        .currentCity(
                                request.getCurrentCity()
                        )
                        .currentCountry(
                                request.getCurrentCountry()
                        )
                        .remarks(request.getRemarks())
                        .status(AlumniStatus.ACTIVE)
                        .build();

        Alumni saved =
                alumniRepository.save(alumni);

        return mapToResponse(saved);
    }

    @Override
    public AlumniResponse update(
            Long id,
            AlumniRequest request
    ) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Alumni alumni =
                alumniRepository
                        .findByIdAndOrganizationId(
                                id,
                                organizationId
                        )
                        .filter(Alumni::getActive)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Alumni not found: " + id
                                )
                        );

        if (!alumni.getStudent().getId()
                .equals(request.getStudentId())) {

            if (alumniRepository
                    .existsByStudentIdAndOrganizationId(
                            request.getStudentId(),
                            organizationId
                    )) {

                throw new DuplicateResourceException(
                        "Student",
                        request.getStudentId().toString()
                );
            }

            Student student =
                    studentRepository
                            .findByIdAndActiveTrue(
                                    request.getStudentId()
                            )
                            .filter(
                                    s -> organizationId.equals(
                                            s.getOrganization().getId()
                                    )
                            )
                            .orElseThrow(
                                    () -> new ResourceNotFoundException(
                                            "Student not found: "
                                                    + request.getStudentId()
                                    )
                            );

            alumni.setStudent(student);
        }

        alumni.setLeavingDate(
                request.getLeavingDate()
        );

        alumni.setGraduationYear(
                request.getGraduationYear()
        );

        alumni.setLastClassName(
                request.getLastClassName()
        );

        alumni.setLastAcademicSession(
                request.getLastAcademicSession()
        );

        alumni.setHigherEducation(
                request.getHigherEducation()
        );

        alumni.setCurrentOrganization(
                request.getCurrentOrganization()
        );

        alumni.setCurrentDesignation(
                request.getCurrentDesignation()
        );

        alumni.setCurrentCity(
                request.getCurrentCity()
        );

        alumni.setCurrentCountry(
                request.getCurrentCountry()
        );

        alumni.setRemarks(
                request.getRemarks()
        );

        return mapToResponse(
                alumniRepository.save(alumni)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AlumniResponse getById(Long id) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Alumni alumni =
                alumniRepository
                        .findByIdAndOrganizationId(
                                id,
                                organizationId
                        )
                        .filter(Alumni::getActive)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Alumni not found: " + id
                                )
                        );

        return mapToResponse(alumni);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlumniResponse> getAll(
            Pageable pageable
    ) {

        Long organizationId =
                currentUserService.getOrganizationId();

        return alumniRepository
                .findByOrganizationIdAndActiveTrue(
                        organizationId,
                        pageable
                )
                .map(this::mapToResponse);
    }

    @Override
    public void activate(Long id) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Alumni alumni =
                alumniRepository
                        .findByIdAndOrganizationId(
                                id,
                                organizationId
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Alumni not found: " + id
                                )
                        );

        alumni.setActive(true);
        alumni.setStatus(AlumniStatus.ACTIVE);

        alumniRepository.save(alumni);
    }

    @Override
    public void deactivate(Long id) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Alumni alumni =
                alumniRepository
                        .findByIdAndOrganizationId(
                                id,
                                organizationId
                        )
                        .filter(Alumni::getActive)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Alumni not found: " + id
                                )
                        );

        alumni.setActive(false);
        alumni.setStatus(AlumniStatus.INACTIVE);

        alumniRepository.save(alumni);
    }

    private AlumniResponse mapToResponse(
            Alumni alumni
    ) {

        Student student =
                alumni.getStudent();

        String studentName =
                (
                        safe(student.getFirstName())
                                + " "
                                + safe(student.getLastName())
                ).trim();

        return AlumniResponse.builder()
                .id(alumni.getId())
                .studentId(student.getId())
                .studentCode(student.getStudentCode())
                .admissionNo(student.getAdmissionNo())
                .studentName(studentName)
                .mobile(student.getMobile())
                .email(student.getEmail())
                .organizationId(alumni.getOrganizationId())
                .leavingDate(alumni.getLeavingDate())
                .graduationYear(alumni.getGraduationYear())
                .lastClassName(alumni.getLastClassName())
                .lastAcademicSession(
                        alumni.getLastAcademicSession()
                )
                .higherEducation(
                        alumni.getHigherEducation()
                )
                .currentOrganization(
                        alumni.getCurrentOrganization()
                )
                .currentDesignation(
                        alumni.getCurrentDesignation()
                )
                .currentCity(
                        alumni.getCurrentCity()
                )
                .currentCountry(
                        alumni.getCurrentCountry()
                )
                .remarks(alumni.getRemarks())
                .status(alumni.getStatus())
                .active(alumni.getActive())
                .createdAt(
                        alumni.getCreatedAt() != null
                                ? alumni.getCreatedAt().toString()
                                : null
                )
                .updatedAt(
                        alumni.getUpdatedAt() != null
                                ? alumni.getUpdatedAt().toString()
                                : null
                )
                .build();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}