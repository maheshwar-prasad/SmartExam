package com.smartexam.staff.document.dto;

import jakarta.validation.constraints.Size;

public record StaffDocumentTypeUpdateRequest(

        Long organizationId,

        @Size(max = 100, message = "Document type name must not exceed 100 characters")
        String name,

        @Size(max = 50, message = "Document type code must not exceed 50 characters")
        String code,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        Boolean active
) {
}