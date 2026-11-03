package com.smartexam.institution.campuses.dto;

import java.time.LocalDateTime;

public record CampusResponse(

        Long id,

        Long organizationId,

        String name,

        String code,

        String address,

        String city,

        String state,

        String postalCode,

        String contactNumber,

        String email,

        String description,

        Boolean active,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        String createdBy,

        String updatedBy
) {
}