package com.smartexam.employee.dto;

import com.smartexam.common.enums.Gender;
import com.smartexam.employee.enums.EmployeeType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class EmployeeResponse {

    private Long id;

    private Long organizationId;
    private Long userId;

    private String employeeCode;
    private String firstName;
    private String lastName;
    private String fullName;

    private EmployeeType employeeType;
    private Gender gender;

    private LocalDate dateOfBirth;
    private LocalDate joiningDate;

    private String email;
    private String mobile;
    private String address;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}