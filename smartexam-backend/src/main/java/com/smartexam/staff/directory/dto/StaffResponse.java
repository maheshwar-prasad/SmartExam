package com.smartexam.staff.directory.dto;

import com.smartexam.staff.directory.enums.EmploymentType;
import com.smartexam.staff.directory.enums.StaffStatus;
import com.smartexam.staff.directory.enums.StaffType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {

    private Long id;

    private Long organizationId;

    private String staffCode;

    private String employeeCode;

    private String firstName;

    private String middleName;

    private String lastName;

    private String fullName;

    private String gender;

    private LocalDate dateOfBirth;

    private String email;

    private String phone;

    private String alternatePhone;

    private String profilePhotoUrl;

    private String designation;

    private Long departmentId;

    private StaffType staffType;

    private EmploymentType employmentType;

    private LocalDate joiningDate;

    private String qualification;

    private BigDecimal experienceYears;

    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String emergencyContactRelation;

    private StaffStatus status;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}