package com.smartexam.staff.leave.service;
import com.smartexam.staff.leave.dto.LeaveRequestRequest;
import com.smartexam.staff.leave.dto.LeaveRequestResponse;
import com.smartexam.staff.leave.dto.LeaveRequestUpdateRequest;
import com.smartexam.staff.leave.enums.LeaveRequestStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LeaveRequestService {

    LeaveRequestResponse create(
            LeaveRequestRequest request
    );

    LeaveRequestResponse update(
            Long id,
            LeaveRequestUpdateRequest request
    );

    LeaveRequestResponse getById(Long id);

    List<LeaveRequestResponse> getAll();

    Page<LeaveRequestResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    );

    List<LeaveRequestResponse> getByOrganization(
            Long organizationId
    );

    List<LeaveRequestResponse> getByTeacher(
            Long teacherId
    );

    List<LeaveRequestResponse> getByTeacherAndSession(
            Long teacherId,
            Long academicSessionId
    );

    List<LeaveRequestResponse> getByStatus(
            Long organizationId,
            LeaveRequestStatus status
    );

    LeaveRequestResponse approve(
            Long id,
            Long approvedBy,
            String comments
    );

    LeaveRequestResponse reject(
            Long id,
            Long rejectedBy,
            String comments
    );

    LeaveRequestResponse cancel(Long id);

    LeaveRequestResponse activate(Long id);

    LeaveRequestResponse deactivate(Long id);

    void delete(Long id);
}