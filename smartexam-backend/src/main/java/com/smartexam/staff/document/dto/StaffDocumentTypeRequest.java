package com.smartexam.staff.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StaffDocumentTypeRequest(

        @NotNull(message = "Organization is required")
        Long organizationId,

        @NotBlank(message = "Document type name is required")
        @Size(max = 100, message = "Document type name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Document type code is required")
        @Size(max = 50, message = "Document type code must not exceed 50 characters")
        String code,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        Boolean active
) {
}