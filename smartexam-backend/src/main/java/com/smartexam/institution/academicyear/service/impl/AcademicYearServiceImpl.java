package com.smartexam.institution.academicyear.service.impl;

import com.smartexam.institution.academicyear.dto.AcademicYearRequest;
import com.smartexam.institution.academicyear.dto.AcademicYearResponse;
import com.smartexam.institution.academicyear.entity.AcademicYear;
import com.smartexam.institution.academicyear.repository.AcademicYearRepository;
import com.smartexam.institution.academicyear.service.AcademicYearService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicYearServiceImpl
        implements AcademicYearService {

    private final AcademicYearRepository repository;

    @Override
    public AcademicYearResponse create(
            AcademicYearRequest request
    ) {

        String code = request.code()
                .trim()
                .toUpperCase();

        if (repository.existsByOrganizationIdAndCode(
                request.organizationId(),
                code
        )) {
            throw new IllegalArgumentException(
                    "Academic year code already exists"
            );
        }

        if (Boolean.TRUE.equals(request.currentYear())) {
            deactivateCurrentYear(
                    request.organizationId()
            );
        }

        AcademicYear academicYear = new AcademicYear();

        academicYear.setOrganizationId(
                request.organizationId()
        );

        academicYear.setName(
                request.name().trim()
        );

        academicYear.setCode(code);

        academicYear.setStartDate(
                request.startDate()
        );

        academicYear.setEndDate(
                request.endDate()
        );

        academicYear.setActive(
                request.active()
        );

        academicYear.setCurrentYear(
                request.currentYear()
        );

        AcademicYear saved = repository.save(
                academicYear
        );

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicYearResponse getById(
            Long id,
            Long organizationId
    ) {

        AcademicYear academicYear =
                repository.findByIdAndOrganizationId(
                        id,
                        organizationId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Academic year not found"
                        )
                );

        return toResponse(academicYear);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicYearResponse> getAll(
            Long organizationId,
            Pageable pageable
    ) {

        return repository
                .findByOrganizationId(
                        organizationId,
                        pageable
                )
                .map(this::toResponse);
    }

    @Override
    public AcademicYearResponse update(
            Long id,
            Long organizationId,
            AcademicYearRequest request
    ) {

        AcademicYear academicYear =
                repository.findByIdAndOrganizationId(
                        id,
                        organizationId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Academic year not found"
                        )
                );

        String code = request.code()
                .trim()
                .toUpperCase();

        if (repository.existsByOrganizationIdAndCodeAndIdNot(
                organizationId,
                code,
                id
        )) {
            throw new IllegalArgumentException(
                    "Academic year code already exists"
            );
        }

        if (Boolean.TRUE.equals(request.currentYear())) {
            deactivateCurrentYear(
                    organizationId
            );
        }

        academicYear.setName(
                request.name().trim()
        );

        academicYear.setCode(code);

        academicYear.setStartDate(
                request.startDate()
        );

        academicYear.setEndDate(
                request.endDate()
        );

        academicYear.setActive(
                request.active()
        );

        academicYear.setCurrentYear(
                request.currentYear()
        );

        AcademicYear updated = repository.save(
                academicYear
        );

        return toResponse(updated);
    }

    @Override
    public void delete(
            Long id,
            Long organizationId
    ) {

        AcademicYear academicYear =
                repository.findByIdAndOrganizationId(
                        id,
                        organizationId
                ).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Academic year not found"
                        )
                );

        repository.delete(academicYear);
    }

    private void deactivateCurrentYear(
            Long organizationId
    ) {

        repository
                .findByOrganizationId(
                        organizationId,
                        Pageable.unpaged()
                )
                .forEach(year ->
                        year.setCurrentYear(false)
                );
    }

    private AcademicYearResponse toResponse(
            AcademicYear academicYear
    ) {

        return new AcademicYearResponse(
                academicYear.getId(),
                academicYear.getOrganizationId(),
                academicYear.getName(),
                academicYear.getCode(),
                academicYear.getStartDate(),
                academicYear.getEndDate(),
                academicYear.getActive(),
                academicYear.getCurrentYear()
        );
    }
}