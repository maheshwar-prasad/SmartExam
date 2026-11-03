package com.smartexam.employee.dto;

import com.smartexam.common.enums.Gender;
import com.smartexam.employee.enums.EmployeeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeRequest {

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Employee code is required")
    @Size(max = 30, message = "Employee code must not exceed 30 characters")
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @NotNull(message = "Employee type is required")
    private EmployeeType employeeType;

    private Gender gender;

    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @Email(message = "Please provide a valid email address")
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String mobile;

    @Size(max = 500)
    private String address;

    private Boolean active = true;
}