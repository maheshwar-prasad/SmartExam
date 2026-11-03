package com.smartexam.staff.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StaffDocumentRequest(

        @NotNull(message = "Organization is required")
        Long organizationId,

        @NotNull(message = "Teacher is required")
        Long teacherId,

        @NotNull(message = "Document type is required")
        Long documentTypeId,

        @Size(max = 100, message = "Document number must not exceed 100 characters")
        String documentNumber,

        @NotBlank(message = "Document name is required")
        @Size(max = 255, message = "Document name must not exceed 255 characters")
        String documentName,

        LocalDate issueDate,

        LocalDate expiryDate,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        Boolean active
) {
}