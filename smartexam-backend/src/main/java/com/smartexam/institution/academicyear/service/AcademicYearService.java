package com.smartexam.institution.academicyear.service;

import com.smartexam.institution.academicyear.dto.AcademicYearRequest;
import com.smartexam.institution.academicyear.dto.AcademicYearResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademicYearService {

    AcademicYearResponse create(
            AcademicYearRequest request
    );

    AcademicYearResponse getById(
            Long id,
            Long organizationId
    );

    Page<AcademicYearResponse> getAll(
            Long organizationId,
            Pageable pageable
    );

    AcademicYearResponse update(
            Long id,
            Long organizationId,
            AcademicYearRequest request
    );

    void delete(
            Long id,
            Long organizationId
    );
}