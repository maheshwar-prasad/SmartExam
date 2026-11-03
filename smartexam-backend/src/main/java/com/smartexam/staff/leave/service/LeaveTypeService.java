package com.smartexam.staff.leave.service;

import com.smartexam.staff.leave.dto.LeaveTypeRequest;
import com.smartexam.staff.leave.dto.LeaveTypeResponse;
import com.smartexam.staff.leave.dto.LeaveTypeUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LeaveTypeService {

    LeaveTypeResponse create(LeaveTypeRequest request);

    LeaveTypeResponse update(
            Long id,
            LeaveTypeUpdateRequest request
    );

    LeaveTypeResponse getById(Long id);

    List<LeaveTypeResponse> getAll();

    Page<LeaveTypeResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    );

    List<LeaveTypeResponse> getByOrganization(
            Long organizationId
    );

    LeaveTypeResponse activate(Long id);

    LeaveTypeResponse deactivate(Long id);

    void delete(Long id);
}