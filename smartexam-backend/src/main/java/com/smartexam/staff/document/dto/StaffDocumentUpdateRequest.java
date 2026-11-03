package com.smartexam.staff.document.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StaffDocumentUpdateRequest(

        Long organizationId,

        Long teacherId,

        Long documentTypeId,

        @Size(max = 100, message = "Document number must not exceed 100 characters")
        String documentNumber,

        @Size(max = 255, message = "Document name must not exceed 255 characters")
        String documentName,

        LocalDate issueDate,

        LocalDate expiryDate,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        Boolean active
) {
}