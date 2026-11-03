package com.smartexam.examresult.service;

import com.smartexam.examresult.dto.ExamResultRequest;
import com.smartexam.examresult.dto.ExamResultResponse;
import com.smartexam.examresult.dto.ExamResultSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExamResultService {

    /**
     * Generate result for a student and exam.
     */
    ExamResultResponse generate(
            ExamResultRequest request
    );

    /**
     * Get result by ID.
     */
    ExamResultResponse getById(
            Long id
    );

    /**
     * Get all active results.
     */
    List<ExamResultResponse> getAll();

    /**
     * Get results for a student.
     */
    List<ExamResultResponse> getByStudentId(
            Long studentId
    );

    /**
     * Get results for an exam.
     */
    List<ExamResultResponse> getByExamId(
            Long examId
    );

    /**
     * Get result for a specific student and exam.
     */
    ExamResultResponse getByStudentAndExam(
            Long studentId,
            Long examId
    );

    /**
     * Get results by organization.
     */
    List<ExamResultResponse> getByOrganizationId(
            Long organizationId
    );

    /**
     * Get paginated active results.
     */
    Page<ExamResultResponse> getPage(
            int page,
            int size
    );

    /**
     * Publish result.
     */
    ExamResultResponse publish(
            Long id
    );

    /**
     * Unpublish result.
     */
    ExamResultResponse unpublish(
            Long id
    );

    /**
     * Delete result.
     */
    void delete(
            Long id
    );

    Page<ExamResultResponse> search(
            ExamResultSearchRequest request,
            int page,
            int size
    );
}