package com.smartexam.institution.campuses.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CampusCreateRequest(

        @NotNull(message = "Organization is required")
        Long organizationId,

        @NotBlank(message = "Campus name is required")
        @Size(max = 150, message = "Campus name must not exceed 150 characters")
        String name,

        @NotBlank(message = "Campus code is required")
        @Size(max = 50, message = "Campus code must not exceed 50 characters")
        String code,

        @Size(max = 500, message = "Address must not exceed 500 characters")
        String address,

        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @Size(max = 100, message = "State must not exceed 100 characters")
        String state,

        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @Size(max = 30, message = "Contact number must not exceed 30 characters")
        String contactNumber,

        @Email(message = "Invalid email address")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        Boolean active
) {
}