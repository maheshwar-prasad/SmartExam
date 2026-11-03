package com.smartexam.institution.settings.dto;

import java.time.LocalDateTime;

public record InstitutionSettingsResponse(

        Long id,

        Long organizationId,

        String institutionName,

        String institutionCode,

        String email,

        String phone,

        String website,

        String address,

        String city,

        String state,

        String country,

        String postalCode,

        String logoUrl,

        Boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}