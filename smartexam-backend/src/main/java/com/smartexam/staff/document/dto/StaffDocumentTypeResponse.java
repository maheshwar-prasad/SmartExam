package com.smartexam.staff.document.dto;

import java.time.LocalDateTime;

public record StaffDocumentTypeResponse(

        Long id,

        Long organizationId,
        String organizationName,

        String name,
        String code,
        String description,

        Boolean active,

        LocalDateTime createdAt,
        Long createdBy,

        LocalDateTime updatedAt,
        Long updatedBy
) {
}