package com.smartexam.institution.program.service;

import com.smartexam.institution.program.dto.ProgramCreateRequest;
import com.smartexam.institution.program.dto.ProgramResponse;
import com.smartexam.institution.program.dto.ProgramUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProgramService {

    List<ProgramResponse> getAllPrograms(
            Long organizationId
    );

    ProgramResponse getProgramById(
            Long id
    );

    ProgramResponse createProgram(
            ProgramCreateRequest request
    );

    ProgramResponse updateProgram(
            Long id,
            ProgramUpdateRequest request
    );

    void activateProgram(
            Long id
    );

    void deactivateProgram(
            Long id
    );

    void deleteProgram(
            Long id
    );

    Page<ProgramResponse> getAllPrograms(
            Long organizationId,
            int page,
            int size,
            String sortBy,
            String direction
    );

}