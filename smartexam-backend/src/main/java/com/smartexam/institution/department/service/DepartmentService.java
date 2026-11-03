
package com.smartexam.institution.department.service;

import com.smartexam.common.dto.PageResponse;
import com.smartexam.institution.department.dto.DepartmentCreateRequest;
import com.smartexam.institution.department.dto.DepartmentResponse;
import com.smartexam.institution.department.dto.DepartmentUpdateRequest;

public interface DepartmentService {


    DepartmentResponse create(
            DepartmentCreateRequest request
    );


    DepartmentResponse update(
            Long id,
            DepartmentUpdateRequest request
    );


    DepartmentResponse getById(
            Long id
    );


    PageResponse<DepartmentResponse> getDepartments(
            int page,
            int size,
            String sortBy,
            String direction,
            Long organizationId,
            String search
    );


    void delete(
            Long id
    );


    DepartmentResponse activate(
            Long id
    );


    DepartmentResponse deactivate(
            Long id
    );

}