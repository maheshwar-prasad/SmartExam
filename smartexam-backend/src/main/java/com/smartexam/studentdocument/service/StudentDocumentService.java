package com.smartexam.studentdocument.service;

import com.smartexam.studentdocument.dto.StudentDocumentResponse;
import com.smartexam.studentdocument.enums.StudentDocumentType;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentDocumentService {

    StudentDocumentResponse upload(
            Long studentId,
            StudentDocumentType documentType,
            String documentName,
            String documentNumber,
            String remarks,
            MultipartFile file
    );

    StudentDocumentResponse getById(Long id);

    List<StudentDocumentResponse> getByStudentId(Long studentId);

    Resource download(Long id);

    void deactivate(Long id);

    void activate(Long id);
}