package com.smartexam.studentdocument.dto;

import com.smartexam.studentdocument.enums.StudentDocumentType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDocumentResponse {

    private Long id;

    private Long studentId;

    private String studentName;

    private String studentCode;

    private StudentDocumentType documentType;

    private String documentName;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    private String documentNumber;

    private String remarks;

    private Boolean active;

    private String createdAt;

    private String updatedAt;
}