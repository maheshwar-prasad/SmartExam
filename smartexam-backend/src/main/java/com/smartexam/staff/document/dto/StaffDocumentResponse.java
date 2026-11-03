package com.smartexam.staff.document.dto;

import com.smartexam.staff.document.enums.DocumentVerificationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StaffDocumentResponse(

        Long id,

        Long organizationId,
        String organizationName,

        Long teacherId,
        String teacherName,
        String teacherEmployeeCode,

        Long documentTypeId,
        String documentTypeName,
        String documentTypeCode,

        String documentNumber,
        String documentName,

        LocalDate issueDate,
        LocalDate expiryDate,

        String description,

        String fileName,
        String fileType,
        Long fileSize,

        DocumentVerificationStatus verificationStatus,

        Long verifiedBy,
        LocalDateTime verifiedAt,
        String verificationComments,

        Boolean active,

        LocalDateTime createdAt,
        Long createdBy,

        LocalDateTime updatedAt,
        Long updatedBy
) {
}