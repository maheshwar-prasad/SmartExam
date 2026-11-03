package com.smartexam.employee.service;

import com.smartexam.employee.dto.EmployeeRequest;
import com.smartexam.employee.dto.EmployeeResponse;
import com.smartexam.employee.enums.EmployeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeRequest request
    );

    EmployeeResponse getEmployeeById(Long employeeId);

    Page<EmployeeResponse> getEmployees(
            Long organizationId,
            Pageable pageable
    );

    Page<EmployeeResponse> searchEmployees(
            Long organizationId,
            String search,
            EmployeeType employeeType,
            Boolean active,
            Pageable pageable
    );

    void deactivateEmployee(Long employeeId);
}