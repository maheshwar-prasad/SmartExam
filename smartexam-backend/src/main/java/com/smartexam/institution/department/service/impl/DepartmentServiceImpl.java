
package com.smartexam.institution.department.service.impl;

import com.smartexam.common.dto.PageResponse;
import com.smartexam.institution.department.dto.DepartmentCreateRequest;
import com.smartexam.institution.department.dto.DepartmentResponse;
import com.smartexam.institution.department.dto.DepartmentUpdateRequest;

import com.smartexam.institution.department.entity.Department;

import com.smartexam.institution.department.exception.DepartmentNotFoundException;

import com.smartexam.institution.department.repository.DepartmentRepository;

import com.smartexam.institution.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl
        implements DepartmentService {


    private final DepartmentRepository departmentRepository;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public DepartmentResponse create(
            DepartmentCreateRequest request
    ) {

        String code =
                request.code()
                        .trim()
                        .toUpperCase();

        validateUniqueCode(
                request.organizationId(),
                code,
                null
        );

        Department department =
                Department.builder()

                        .organizationId(
                                request.organizationId()
                        )

                        .campusId(
                                request.campusId()
                        )

                        .name(
                                request.name().trim()
                        )

                        .code(code)

                        .description(
                                request.description()
                        )

                        .active(true)

                        .build();


        Department saved =
                departmentRepository.save(
                        department
                );


        return toResponse(saved);

    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public DepartmentResponse update(
            Long id,
            DepartmentUpdateRequest request
    ) {

        Department department =
                getDepartmentEntity(id);


        String code =
                request.code()
                        .trim()
                        .toUpperCase();


        validateUniqueCode(
                request.organizationId(),
                code,
                id
        );


        department.setOrganizationId(
                request.organizationId()
        );

        department.setCampusId(
                request.campusId()
        );

        department.setName(
                request.name().trim()
        );

        department.setCode(code);

        department.setDescription(
                request.description()
        );


        Department updated =
                departmentRepository.save(
                        department
                );


        return toResponse(updated);

    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getById(
            Long id
    ) {

        Department department =
                getDepartmentEntity(id);


        return toResponse(department);

    }


    // =========================================================
    // GET PAGINATED
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DepartmentResponse> getDepartments(

            int page,

            int size,

            String sortBy,

            String direction,

            Long organizationId,

            String search

    ) {


        if (page < 0) {

            page = 0;

        }


        if (size <= 0 || size > 100) {

            size = 20;

        }


        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)

                        ? Sort.Direction.DESC

                        : Sort.Direction.ASC;


        String safeSortBy =
                resolveSortField(sortBy);


        Pageable pageable =
                PageRequest.of(

                        page,

                        size,

                        Sort.by(
                                sortDirection,
                                safeSortBy
                        )

                );


        Page<Department> result;


        if (organizationId != null) {


            if (search != null && !search.isBlank()) {

                String keyword =
                        search.trim();


                result =
                        departmentRepository
                                .findByOrganizationIdAndNameContainingIgnoreCase(
                                        organizationId,
                                        keyword,
                                        pageable
                                );


            } else {

                result =
                        departmentRepository
                                .findByOrganizationId(
                                        organizationId,
                                        pageable
                                );

            }


        } else {

            result =
                    departmentRepository.findAll(
                            pageable
                    );

        }


        return new PageResponse<>(

                result.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),

                result.getNumber(),

                result.getSize(),

                result.getTotalElements(),

                result.getTotalPages(),

                result.isFirst(),

                result.isLast()

        );

    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void delete(
            Long id
    ) {

        Department department =
                getDepartmentEntity(id);


        // Soft delete

        department.setActive(false);


        departmentRepository.save(
                department
        );

    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public DepartmentResponse activate(
            Long id
    ) {

        Department department =
                getDepartmentEntity(id);


        department.setActive(true);


        Department updated =
                departmentRepository.save(
                        department
                );


        return toResponse(updated);

    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public DepartmentResponse deactivate(
            Long id
    ) {

        Department department =
                getDepartmentEntity(id);


        department.setActive(false);


        Department updated =
                departmentRepository.save(
                        department
                );


        return toResponse(updated);

    }


    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    private Department getDepartmentEntity(
            Long id
    ) {

        return departmentRepository
                .findById(id)
                .orElseThrow(

                        () ->
                                new DepartmentNotFoundException(id)

                );

    }


    private void validateUniqueCode(

            Long organizationId,

            String code,

            Long id

    ) {


        boolean exists;


        if (id == null) {

            exists =
                    departmentRepository
                            .existsByOrganizationIdAndCodeIgnoreCase(
                                    organizationId,
                                    code
                            );

        } else {

            exists =
                    departmentRepository
                            .existsByOrganizationIdAndCodeIgnoreCaseAndIdNot(
                                    organizationId,
                                    code,
                                    id
                            );

        }


        if (exists) {

            throw new IllegalArgumentException(

                    "Department code already exists: "
                            + code

            );

        }

    }


    private String resolveSortField(
            String sortBy
    ) {

        if (sortBy == null || sortBy.isBlank()) {

            return "name";

        }


        return switch (
                sortBy
                        .trim()
                        .toLowerCase()
                ) {

            case "name" -> "name";

            case "code" -> "code";

            case "createdat" -> "createdAt";

            case "active" -> "active";

            default -> "name";

        };

    }


    private DepartmentResponse toResponse(
            Department department
    ) {

        return new DepartmentResponse(

                department.getId(),

                department.getOrganizationId(),

                department.getCampusId(),

                department.getName(),

                department.getCode(),

                department.getDescription(),

                department.getActive(),

                department.getCreatedAt(),

                department.getCreatedBy(),

                department.getUpdatedAt(),

                department.getUpdatedBy()

        );

    }

}