package com.smartexam.examregistration.service;

import com.smartexam.examregistration.dto.StudentExamRegistrationRequest;
import com.smartexam.examregistration.dto.StudentExamRegistrationResponse;
import com.smartexam.examregistration.dto.StudentExamRegistrationSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentExamRegistrationService {

    /**
     * Register a student for an exam.
     */
    StudentExamRegistrationResponse create(
            StudentExamRegistrationRequest request
    );

    /**
     * Update student exam registration.
     */
    StudentExamRegistrationResponse update(
            Long id,
            StudentExamRegistrationRequest request
    );

    /**
     * Get registration by ID.
     */
    StudentExamRegistrationResponse getById(
            Long id
    );

    /**
     * Get all active registrations.
     */
    List<StudentExamRegistrationResponse> getAll();

    /**
     * Get registrations by student ID.
     */
    List<StudentExamRegistrationResponse> getByStudentId(
            Long studentId
    );

    /**
     * Get registrations by exam ID.
     */
    List<StudentExamRegistrationResponse> getByExamId(
            Long examId
    );

    /**
     * Get registrations by organization ID.
     */
    List<StudentExamRegistrationResponse> getByOrganizationId(
            Long organizationId
    );

    /**
     * Get paginated active registrations.
     */
    Page<StudentExamRegistrationResponse> getPage(
            int page,
            int size
    );

    /**
     * Cancel student exam registration.
     */
    StudentExamRegistrationResponse cancel(
            Long id
    );

    /**
     * Activate registration.
     */
    StudentExamRegistrationResponse activate(
            Long id
    );

    /**
     * Deactivate registration.
     */
    StudentExamRegistrationResponse deactivate(
            Long id
    );

    /**
     * Delete registration.
     */
    void delete(
            Long id
    );
    Page<StudentExamRegistrationResponse> search(
            StudentExamRegistrationSearchRequest request,
            int page,
            int size
    );
}