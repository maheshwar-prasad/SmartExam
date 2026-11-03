package com.smartexam.exam.service;

import com.smartexam.exam.dto.ExamRequest;
import com.smartexam.exam.dto.ExamResponse;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ExamService {

    ExamResponse create(ExamRequest request);

    ExamResponse getById(Long id);

    List<ExamResponse> getAll();

    List<ExamResponse> getByOrganizationId(Long organizationId);

    List<ExamResponse> getByAcademicSessionId(Long academicSessionId);

    List<ExamResponse> getByClassAndSection(
            Long academicSessionId,
            Long classRoomId,
            Long sectionId
    );

    ExamResponse update(
            Long id,
            ExamRequest request
    );

    void delete(Long id);

    ExamResponse activate(Long id);

    ExamResponse deactivate(Long id);

    Page<ExamResponse> getPage(int page, int size);

}