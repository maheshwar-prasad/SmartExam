package com.smartexam.institution.settings.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateInstitutionSettingsRequest(

        @NotNull(message = "Organization ID must not be null")
        Long organizationId,

        @NotBlank(message = "Institution name must not be blank")
        @Size(
                max = 150,
                message = "Institution name must not exceed 150 characters"
        )
        String institutionName,

        @NotBlank(message = "Institution code must not be blank")
        @Size(
                max = 50,
                message = "Institution code must not exceed 50 characters"
        )
        String institutionCode,

        @Email(message = "Email must be valid")
        @Size(
                max = 150,
                message = "Email must not exceed 150 characters"
        )
        String email,

        @Size(
                max = 20,
                message = "Phone must not exceed 20 characters"
        )
        String phone,

        @Size(
                max = 255,
                message = "Website must not exceed 255 characters"
        )
        String website,

        @Size(
                max = 500,
                message = "Address must not exceed 500 characters"
        )
        String address,

        @Size(
                max = 100,
                message = "City must not exceed 100 characters"
        )
        String city,

        @Size(
                max = 100,
                message = "State must not exceed 100 characters"
        )
        String state,

        @Size(
                max = 100,
                message = "Country must not exceed 100 characters"
        )
        String country,

        @Size(
                max = 20,
                message = "Postal code must not exceed 20 characters"
        )
        String postalCode,

        @Size(
                max = 500,
                message = "Logo URL must not exceed 500 characters"
        )
        String logoUrl,

        Boolean active
) {
}