package com.smartexam.parentguardian.service.impl;

import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.parentguardian.dto.StudentParentGuardianRequest;
import com.smartexam.parentguardian.dto.StudentParentGuardianResponse;
import com.smartexam.parentguardian.entity.ParentGuardian;
import com.smartexam.parentguardian.entity.StudentParentGuardian;
import com.smartexam.parentguardian.enums.ParentGuardianRelation;
import com.smartexam.parentguardian.repository.ParentGuardianRepository;
import com.smartexam.parentguardian.repository.StudentParentGuardianRepository;
import com.smartexam.parentguardian.service.StudentParentGuardianService;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.student.entity.Student;
import com.smartexam.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentParentGuardianServiceImpl
        implements StudentParentGuardianService {

    private final StudentParentGuardianRepository relationshipRepository;

    private final ParentGuardianRepository parentGuardianRepository;

    private final StudentRepository studentRepository;
    private final CurrentUserService currentUserService;


    // =========================================================
    // Create Relationship
    // =========================================================

    @Override
    public StudentParentGuardianResponse create(
            StudentParentGuardianRequest request) {

        Student student =
                getStudent(request.getStudentId());

        ParentGuardian parentGuardian =
                getParentGuardian(request.getParentGuardianId());

        validateSameOrganization(
                student,
                parentGuardian
        );

        validateDuplicateRelationship(request);

        if (Boolean.TRUE.equals(request.getPrimaryContact())) {

            clearExistingPrimaryContact(
                    student.getId()
            );
        }

        StudentParentGuardian relationship =
                StudentParentGuardian.builder()
                        .student(student)
                        .parentGuardian(parentGuardian)
                        .relation(request.getRelation())
                        .primaryContact(
                                Boolean.TRUE.equals(
                                        request.getPrimaryContact()
                                )
                        )
                        .emergencyContact(
                                Boolean.TRUE.equals(
                                        request.getEmergencyContact()
                                )
                        )
                        .remarks(
                                normalize(request.getRemarks())
                        )
                        .active(true)
                        .build();

        StudentParentGuardian saved =
                relationshipRepository.save(
                        relationship
                );

        log.info(
                "Student parent/guardian relationship created. " +
                        "studentId={}, parentGuardianId={}, relation={}",
                student.getId(),
                parentGuardian.getId(),
                request.getRelation()
        );

        return toResponse(saved);
    }

    // =========================================================
    // Update Relationship
    // =========================================================

    @Override
    public StudentParentGuardianResponse update(
            Long id,
            StudentParentGuardianRequest request) {

        StudentParentGuardian relationship =
                getRelationship(id);

        Student student =
                getStudent(request.getStudentId());

        ParentGuardian parentGuardian =
                getParentGuardian(
                        request.getParentGuardianId()
                );

        validateSameOrganization(
                student,
                parentGuardian
        );

        validateDuplicateRelationship(
                request,
                id
        );

        if (Boolean.TRUE.equals(request.getPrimaryContact())) {

            clearExistingPrimaryContact(
                    student.getId(),
                    id
            );
        }

        relationship.setStudent(student);

        relationship.setParentGuardian(
                parentGuardian
        );

        relationship.setRelation(
                request.getRelation()
        );

        relationship.setPrimaryContact(
                Boolean.TRUE.equals(
                        request.getPrimaryContact()
                )
        );

        relationship.setEmergencyContact(
                Boolean.TRUE.equals(
                        request.getEmergencyContact()
                )
        );

        relationship.setRemarks(
                normalize(request.getRemarks())
        );

        StudentParentGuardian saved =
                relationshipRepository.save(
                        relationship
                );

        log.info(
                "Student parent/guardian relationship updated. id={}",
                id
        );

        return toResponse(saved);
    }

    // =========================================================
    // Get By Student
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<StudentParentGuardianResponse> getByStudentId(
            Long studentId) {

        Student student =
                getStudent(studentId);

        return relationshipRepository
                .findByStudentIdAndActiveTrue(
                        student.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // Get By ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public StudentParentGuardianResponse getById(
            Long id) {

        return toResponse(
                getRelationship(id)
        );
    }

    // =========================================================
    // Deactivate
    // =========================================================

    @Override
    public void deactivate(Long id) {

        StudentParentGuardian relationship =
                getRelationship(id);

        if (!Boolean.TRUE.equals(
                relationship.getActive())) {

            return;
        }

        relationship.setActive(false);

        relationshipRepository.save(
                relationship
        );

        log.info(
                "Student parent/guardian relationship deactivated. id={}",
                id
        );
    }

    // =========================================================
    // Activate
    // =========================================================

    @Override
    public void activate(Long id) {

        StudentParentGuardian relationship =
                getRelationship(id);

        if (Boolean.TRUE.equals(
                relationship.getActive())) {

            return;
        }

        relationship.setActive(true);

        relationshipRepository.save(
                relationship
        );

        log.info(
                "Student parent/guardian relationship activated. id={}",
                id
        );
    }

    // =========================================================
    // Find Student
    // =========================================================

    private Student getStudent(Long studentId) {

        return studentRepository
                .findById(studentId)
                .filter(Student::getActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : "
                                        + studentId
                        )
                );
    }

    // =========================================================
    // Find Parent / Guardian
    // =========================================================

    private ParentGuardian getParentGuardian(
            Long parentGuardianId) {

        return parentGuardianRepository
                .findByIdAndActiveTrue(
                        parentGuardianId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parent/Guardian not found with id : "
                                        + parentGuardianId
                        )
                );
    }

    // =========================================================
    // Find Relationship
    // =========================================================

    private StudentParentGuardian getRelationship(
            Long id) {

        return relationshipRepository
                .findById(id)
                .filter(
                        relationship ->
                                Boolean.TRUE.equals(
                                        relationship.getActive()
                                )
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student parent/guardian relationship " +
                                        "not found with id : " + id
                        )
                );
    }

    // =========================================================
    // Organization Validation
    // =========================================================

    private void validateSameOrganization(
            Student student,
            ParentGuardian parentGuardian) {

        Long currentOrganizationId =
                currentUserService.getOrganizationId();

        Long studentOrganizationId =
                student.getOrganization() != null
                        ? student.getOrganization().getId()
                        : null;

        Long parentOrganizationId =
                parentGuardian.getOrganizationId();

        if (studentOrganizationId == null ||
                !currentOrganizationId.equals(studentOrganizationId)) {

            throw new ResourceNotFoundException(
                    "Student not found in the current organization."
            );
        }

        if (parentOrganizationId == null ||
                !currentOrganizationId.equals(parentOrganizationId)) {

            throw new ResourceNotFoundException(
                    "Parent/Guardian not found in the current organization."
            );
        }
    }

    // =========================================================
    // Duplicate Relationship
    // =========================================================

    private void validateDuplicateRelationship(
            StudentParentGuardianRequest request) {

        validateDuplicateRelationship(
                request,
                null
        );
    }

    private void validateDuplicateRelationship(
            StudentParentGuardianRequest request,
            Long excludeId) {

        relationshipRepository
                .findByStudentIdAndParentGuardianIdAndRelation(
                        request.getStudentId(),
                        request.getParentGuardianId(),
                        request.getRelation()
                )
                .ifPresent(existing -> {

                    if (!existing.getId().equals(excludeId) &&
                            Boolean.TRUE.equals(
                                    existing.getActive()
                            )) {

                        throw new DuplicateResourceException(
                                "Student parent/guardian relationship",
                                request.getParentGuardianId()
                                        + "-" + request.getRelation()
                        );
                    }
                });
    }

    // =========================================================
    // Primary Contact
    // =========================================================

    private void clearExistingPrimaryContact(
            Long studentId) {

        clearExistingPrimaryContact(
                studentId,
                null
        );
    }

    private void clearExistingPrimaryContact(
            Long studentId,
            Long excludeId) {

        relationshipRepository
                .findByStudentIdAndActiveTrue(studentId)
                .stream()
                .filter(
                        relationship ->
                                Boolean.TRUE.equals(
                                        relationship.getPrimaryContact()
                                )
                )
                .filter(
                        relationship ->
                                excludeId == null ||
                                        !relationship.getId()
                                                .equals(excludeId)
                )
                .forEach(
                        relationship -> {
                            relationship.setPrimaryContact(false);

                            relationshipRepository.save(
                                    relationship
                            );
                        }
                );
    }

    // =========================================================
    // Response Mapping
    // =========================================================

    private StudentParentGuardianResponse toResponse(
            StudentParentGuardian relationship) {

        Student student =
                relationship.getStudent();

        ParentGuardian parentGuardian =
                relationship.getParentGuardian();

        String studentName =
                buildFullName(
                        student.getFirstName(),
                        student.getLastName()
                );

        String parentGuardianName =
                buildFullName(
                        parentGuardian.getFirstName(),
                        parentGuardian.getLastName()
                );

        return StudentParentGuardianResponse
                .builder()
                .id(relationship.getId())
                .studentId(student.getId())
                .studentName(studentName)
                .parentGuardianId(parentGuardian.getId())
                .parentGuardianName(parentGuardianName)
                .relation(relationship.getRelation())
                .mobile(parentGuardian.getMobile())
                .email(parentGuardian.getEmail())
                .primaryContact(
                        relationship.getPrimaryContact()
                )
                .emergencyContact(
                        relationship.getEmergencyContact()
                )
                .remarks(relationship.getRemarks())
                .active(relationship.getActive())
                .build();
    }

    // =========================================================
    // Helpers
    // =========================================================

    private String normalize(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty()
                ? null
                : trimmed;
    }

    private String buildFullName(
            String firstName,
            String lastName) {

        String first =
                firstName == null
                        ? ""
                        : firstName.trim();

        String last =
                lastName == null
                        ? ""
                        : lastName.trim();

        return (first + " " + last).trim();
    }
}