package com.smartexam.examsubject.service;

import com.smartexam.examsubject.dto.ExamSubjectRequest;
import com.smartexam.examsubject.dto.ExamSubjectResponse;
import com.smartexam.examsubject.dto.ExamSubjectSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExamSubjectService {

    /**
     * Create a new subject for an exam.
     */
    ExamSubjectResponse create(
            ExamSubjectRequest request
    );

    /**
     * Update an existing exam subject.
     */
    ExamSubjectResponse update(
            Long id,
            ExamSubjectRequest request
    );

    /**
     * Get exam subject by ID.
     */
    ExamSubjectResponse getById(
            Long id
    );

    /**
     * Get all active exam subjects.
     */
    List<ExamSubjectResponse> getAll();

    /**
     * Get active exam subjects by exam ID.
     */
    List<ExamSubjectResponse> getByExamId(
            Long examId
    );

    /**
     * Get active exam subjects by subject ID.
     */
    List<ExamSubjectResponse> getBySubjectId(
            Long subjectId
    );

    /**
     * Get active exam subjects by organization ID.
     */
    List<ExamSubjectResponse> getByOrganizationId(
            Long organizationId
    );

    /**
     * Get paginated active exam subjects.
     */
    Page<ExamSubjectResponse> getPage(
            int page,
            int size
    );

    /**
     * Activate exam subject.
     */
    ExamSubjectResponse activate(
            Long id
    );

    /**
     * Deactivate exam subject.
     */
    ExamSubjectResponse deactivate(
            Long id
    );

    /**
     * Delete exam subject.
     */
    void delete(
            Long id
    );

    Page<ExamSubjectResponse> search(
            ExamSubjectSearchRequest request,
            int page,
            int size
    );
}