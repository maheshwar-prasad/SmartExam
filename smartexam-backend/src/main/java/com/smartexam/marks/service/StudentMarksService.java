package com.smartexam.marks.service;

import com.smartexam.marks.dto.StudentMarksRequest;
import com.smartexam.marks.dto.StudentMarksResponse;
import com.smartexam.marks.dto.StudentMarksSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentMarksService {

    /**
     * Create student marks.
     */
    StudentMarksResponse create(
            StudentMarksRequest request
    );

    /**
     * Update student marks.
     */
    StudentMarksResponse update(
            Long id,
            StudentMarksRequest request
    );

    /**
     * Get marks by ID.
     */
    StudentMarksResponse getById(
            Long id
    );

    /**
     * Get all active student marks.
     */
    List<StudentMarksResponse> getAll();

    /**
     * Get marks by student ID.
     */
    List<StudentMarksResponse> getByStudentId(
            Long studentId
    );

    /**
     * Get marks by exam ID.
     */
    List<StudentMarksResponse> getByExamId(
            Long examId
    );

    /**
     * Get marks by Exam Subject ID.
     */
    List<StudentMarksResponse> getByExamSubjectId(
            Long examSubjectId
    );

    /**
     * Get marks by Organization ID.
     */
    List<StudentMarksResponse> getByOrganizationId(
            Long organizationId
    );

    /**
     * Get paginated active marks.
     */
    Page<StudentMarksResponse> getPage(
            int page,
            int size
    );

    /**
     * Publish marks.
     */
    StudentMarksResponse publish(
            Long id
    );

    /**
     * Unpublish marks.
     */
    StudentMarksResponse unpublish(
            Long id
    );

    /**
     * Activate marks record.
     */
    StudentMarksResponse activate(
            Long id
    );

    /**
     * Deactivate marks record.
     */
    StudentMarksResponse deactivate(
            Long id
    );

    /**
     * Delete marks record.
     */
    void delete(
            Long id
    );
    Page<StudentMarksResponse> search(
            StudentMarksSearchRequest request,
            int page,
            int size
    );

}