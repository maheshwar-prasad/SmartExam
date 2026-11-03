package com.smartexam.syllabus.service;

import com.smartexam.syllabus.dto.SyllabusRequest;
import com.smartexam.syllabus.dto.SyllabusResponse;
import com.smartexam.syllabus.dto.SyllabusUpdateRequest;

import java.util.List;

public interface SyllabusService {


    SyllabusResponse create(
            SyllabusRequest request
    );


    SyllabusResponse update(
            Long id,
            SyllabusUpdateRequest request
    );


    SyllabusResponse getById(
            Long id
    );


    List<SyllabusResponse> getAll();


    List<SyllabusResponse> getByOrganization(
            Long organizationId
    );


    List<SyllabusResponse> getByCurriculum(
            Long curriculumId
    );


    List<SyllabusResponse> getByClassSubject(
            Long classSubjectId
    );


    List<SyllabusResponse> getActiveByCurriculum(
            Long curriculumId
    );


    List<SyllabusResponse> getByCurriculumAndClassSubject(
            Long curriculumId,
            Long classSubjectId
    );


    SyllabusResponse activate(
            Long id
    );


    SyllabusResponse deactivate(
            Long id
    );


    void delete(
            Long id
    );
}