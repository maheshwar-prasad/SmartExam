package com.smartexam.academicstructure.service;

import com.smartexam.academicstructure.dto.AcademicStructureRequest;
import com.smartexam.academicstructure.dto.AcademicStructureResponse;
import com.smartexam.academicstructure.dto.AcademicStructureUpdateRequest;

import java.util.List;

public interface AcademicStructureService {

    AcademicStructureResponse create(
            AcademicStructureRequest request
    );

    AcademicStructureResponse update(
            Long id,
            AcademicStructureUpdateRequest request
    );

    AcademicStructureResponse getById(
            Long id
    );

    List<AcademicStructureResponse> getAll();

    List<AcademicStructureResponse> getByOrganization(
            Long organizationId
    );

    List<AcademicStructureResponse> getByAcademicSession(
            Long academicSessionId
    );

    List<AcademicStructureResponse> getByClassRoom(
            Long classRoomId
    );

    List<AcademicStructureResponse> getBySection(
            Long sectionId
    );

    List<AcademicStructureResponse> getBySessionAndClass(
            Long academicSessionId,
            Long classRoomId
    );

    List<AcademicStructureResponse> getActiveByOrganization(
            Long organizationId
    );

    AcademicStructureResponse activate(
            Long id
    );

    AcademicStructureResponse deactivate(
            Long id
    );

    void delete(
            Long id
    );
}