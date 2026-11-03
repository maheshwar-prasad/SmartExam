package com.smartexam.staff.document.service;

import com.smartexam.staff.document.dto.StaffDocumentTypeRequest;
import com.smartexam.staff.document.dto.StaffDocumentTypeResponse;
import com.smartexam.staff.document.dto.StaffDocumentTypeUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StaffDocumentTypeService {

    StaffDocumentTypeResponse create(
            StaffDocumentTypeRequest request
    );

    StaffDocumentTypeResponse update(
            Long id,
            StaffDocumentTypeUpdateRequest request
    );

    StaffDocumentTypeResponse getById(
            Long id
    );

    List<StaffDocumentTypeResponse> getAll();

    Page<StaffDocumentTypeResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    );

    List<StaffDocumentTypeResponse> getByOrganization(
            Long organizationId
    );

    StaffDocumentTypeResponse activate(
            Long id
    );

    StaffDocumentTypeResponse deactivate(
            Long id
    );

    void delete(
            Long id
    );
}