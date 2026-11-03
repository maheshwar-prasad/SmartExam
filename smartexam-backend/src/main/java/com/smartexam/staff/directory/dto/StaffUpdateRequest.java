package com.smartexam.staff.directory.dto;

import com.smartexam.staff.directory.enums.EmploymentType;
import com.smartexam.staff.directory.enums.StaffStatus;
import com.smartexam.staff.directory.enums.StaffType;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class StaffUpdateRequest {

    @NotBlank(message = "Staff code is required")
    @Size(
            max = 50,
            message = "Staff code must not exceed 50 characters"
    )
    private String staffCode;

    @Size(
            max = 50,
            message = "Employee code must not exceed 50 characters"
    )
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Size(
            max = 100,
            message = "First name must not exceed 100 characters"
    )
    private String firstName;

    @Size(
            max = 100,
            message = "Middle name must not exceed 100 characters"
    )
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(
            max = 100,
            message = "Last name must not exceed 100 characters"
    )
    private String lastName;

    @Size(
            max = 20,
            message = "Gender must not exceed 20 characters"
    )
    private String gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(
            max = 150,
            message = "Email must not exceed 150 characters"
    )
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+()\\- ]{7,20}$",
            message = "Please provide a valid phone number"
    )
    private String phone;

    @Pattern(
            regexp = "^[0-9+()\\- ]{7,20}$",
            message = "Please provide a valid alternate phone number"
    )
    private String alternatePhone;

    @Size(
            max = 500,
            message = "Profile photo URL must not exceed 500 characters"
    )
    private String profilePhotoUrl;

    @NotBlank(message = "Designation is required")
    @Size(
            max = 150,
            message = "Designation must not exceed 150 characters"
    )
    private String designation;

    private Long departmentId;

    @NotNull(message = "Staff type is required")
    private StaffType staffType;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @Size(
            max = 255,
            message = "Qualification must not exceed 255 characters"
    )
    private String qualification;

    @DecimalMin(
            value = "0.0",
            message = "Experience years cannot be negative"
    )
    @DecimalMax(
            value = "99.99",
            message = "Experience years must not exceed 99.99"
    )
    private BigDecimal experienceYears;

    @Size(
            max = 500,
            message = "Address must not exceed 500 characters"
    )
    private String address;

    @Size(
            max = 100,
            message = "City must not exceed 100 characters"
    )
    private String city;

    @Size(
            max = 100,
            message = "State must not exceed 100 characters"
    )
    private String state;

    @Size(
            max = 100,
            message = "Country must not exceed 100 characters"
    )
    private String country;

    @Size(
            max = 20,
            message = "Postal code must not exceed 20 characters"
    )
    private String postalCode;

    @Size(
            max = 150,
            message = "Emergency contact name must not exceed 150 characters"
    )
    private String emergencyContactName;

    @Pattern(
            regexp = "^[0-9+()\\- ]{7,20}$",
            message = "Please provide a valid emergency contact phone"
    )
    private String emergencyContactPhone;

    @Size(
            max = 50,
            message = "Emergency contact relation must not exceed 50 characters"
    )
    private String emergencyContactRelation;

    private StaffStatus status;
}