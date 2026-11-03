package com.smartexam.curriculum.service;

import com.smartexam.curriculum.dto.CurriculumRequest;
import com.smartexam.curriculum.dto.CurriculumResponse;
import com.smartexam.curriculum.dto.CurriculumSubjectRequest;
import com.smartexam.curriculum.dto.CurriculumSubjectResponse;
import com.smartexam.curriculum.dto.CurriculumUpdateRequest;

import java.util.List;

public interface CurriculumService {

    CurriculumResponse create(
            CurriculumRequest request
    );

    CurriculumResponse update(
            Long id,
            CurriculumUpdateRequest request
    );

    CurriculumResponse getById(
            Long id
    );

    List<CurriculumResponse> getAll();

    List<CurriculumResponse> getByOrganization(
            Long organizationId
    );

    List<CurriculumResponse> getByAcademicSession(
            Long academicSessionId
    );

    List<CurriculumResponse> getByClassRoom(
            Long classRoomId
    );

    List<CurriculumResponse> getBySessionAndClass(
            Long academicSessionId,
            Long classRoomId
    );

    CurriculumResponse activate(
            Long id
    );

    CurriculumResponse deactivate(
            Long id
    );

    void delete(
            Long id
    );


    // ============================================================
    // CURRICULUM SUBJECTS
    // ============================================================

    CurriculumSubjectResponse addSubject(
            Long curriculumId,
            CurriculumSubjectRequest request
    );

    CurriculumSubjectResponse updateSubject(
            Long curriculumSubjectId,
            CurriculumSubjectRequest request
    );

    List<CurriculumSubjectResponse> getSubjects(
            Long curriculumId
    );

    CurriculumSubjectResponse activateSubject(
            Long curriculumSubjectId
    );

    CurriculumSubjectResponse deactivateSubject(
            Long curriculumSubjectId
    );

    void deleteSubject(
            Long curriculumSubjectId
    );
}