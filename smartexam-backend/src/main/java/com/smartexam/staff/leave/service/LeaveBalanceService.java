package com.smartexam.staff.leave.service;
import com.smartexam.staff.leave.dto.LeaveBalanceRequest;
import com.smartexam.staff.leave.dto.LeaveBalanceResponse;
import com.smartexam.staff.leave.dto.LeaveBalanceUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LeaveBalanceService {

    LeaveBalanceResponse create(
            LeaveBalanceRequest request
    );

    LeaveBalanceResponse update(
            Long id,
            LeaveBalanceUpdateRequest request
    );

    LeaveBalanceResponse getById(Long id);

    List<LeaveBalanceResponse> getAll();

    Page<LeaveBalanceResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    );

    List<LeaveBalanceResponse> getByOrganization(
            Long organizationId
    );

    List<LeaveBalanceResponse> getByTeacher(
            Long teacherId
    );

    List<LeaveBalanceResponse> getByTeacherAndSession(
            Long teacherId,
            Long academicSessionId
    );

    LeaveBalanceResponse activate(Long id);

    LeaveBalanceResponse deactivate(Long id);

    void delete(Long id);
}