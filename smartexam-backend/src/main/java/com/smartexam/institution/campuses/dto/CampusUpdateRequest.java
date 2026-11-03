package com.smartexam.institution.campuses.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CampusUpdateRequest(

        @NotBlank(message = "Campus name is required")
        @Size(max = 150)
        String name,

        @NotBlank(message = "Campus code is required")
        @Size(max = 50)
        String code,

        String address,

        String city,

        String state,

        String postalCode,

        String contactNumber,

        @Email(message = "Invalid email address")
        String email,

        String description,

        Boolean active
) {
}