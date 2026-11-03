package com.smartexam.examschedule.service;

import com.smartexam.examschedule.dto.ExamScheduleRequest;
import com.smartexam.examschedule.dto.ExamScheduleResponse;
import com.smartexam.examschedule.dto.ExamScheduleSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExamScheduleService {

    ExamScheduleResponse create(
            ExamScheduleRequest request
    );

    ExamScheduleResponse update(
            Long id,
            ExamScheduleRequest request
    );

    ExamScheduleResponse getById(
            Long id
    );

    List<ExamScheduleResponse> getAll();

    List<ExamScheduleResponse> getByExamId(
            Long examId
    );

    List<ExamScheduleResponse> getByExamSubjectId(
            Long examSubjectId
    );

    List<ExamScheduleResponse> getByOrganizationId(
            Long organizationId
    );

    Page<ExamScheduleResponse> getPage(
            int page,
            int size
    );

    ExamScheduleResponse activate(
            Long id
    );

    ExamScheduleResponse deactivate(
            Long id
    );

    void delete(
            Long id
    );

    Page<ExamScheduleResponse> search(
            ExamScheduleSearchRequest request,
            int page,
            int size
    );
}