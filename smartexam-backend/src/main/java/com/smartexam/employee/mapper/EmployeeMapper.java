package com.smartexam.employee.mapper;

import com.smartexam.employee.dto.EmployeeResponse;
import com.smartexam.employee.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponse toResponse(Employee employee) {

        if (employee == null) {
            return null;
        }

        return EmployeeResponse.builder()
                .id(employee.getId())
                .organizationId(
                        employee.getOrganization() != null
                                ? employee.getOrganization().getId()
                                : null
                )
                .userId(
                        employee.getUser() != null
                                ? employee.getUser().getId()
                                : null
                )
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .fullName(buildFullName(
                        employee.getFirstName(),
                        employee.getLastName()
                ))
                .employeeType(employee.getEmployeeType())
                .gender(employee.getGender())
                .dateOfBirth(employee.getDateOfBirth())
                .joiningDate(employee.getJoiningDate())
                .email(employee.getEmail())
                .mobile(employee.getMobile())
                .address(employee.getAddress())
                .active(employee.getActive())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    private String buildFullName(String firstName, String lastName) {
        String first = firstName == null ? "" : firstName.trim();
        String last = lastName == null ? "" : lastName.trim();

        return (first + " " + last).trim();
    }
}