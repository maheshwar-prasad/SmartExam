package com.smartexam.staff.document.service;

import com.smartexam.staff.document.dto.StaffDocumentRequest;
import com.smartexam.staff.document.dto.StaffDocumentResponse;
import com.smartexam.staff.document.dto.StaffDocumentUpdateRequest;
import com.smartexam.staff.document.enums.DocumentVerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;

public interface StaffDocumentService {

    StaffDocumentResponse create(
            StaffDocumentRequest request
    );

    StaffDocumentResponse update(
            Long id,
            StaffDocumentUpdateRequest request
    );

    StaffDocumentResponse getById(
            Long id
    );

    List<StaffDocumentResponse> getAll();

    Page<StaffDocumentResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    );

    List<StaffDocumentResponse> getByOrganization(
            Long organizationId
    );

    List<StaffDocumentResponse> getByTeacher(
            Long teacherId
    );

    List<StaffDocumentResponse> getByStatus(
            Long organizationId,
            DocumentVerificationStatus status
    );

    StaffDocumentResponse verify(
            Long id,
            Long verifiedBy,
            String comments
    );

    StaffDocumentResponse reject(
            Long id,
            Long rejectedBy,
            String comments
    );

    StaffDocumentResponse activate(
            Long id
    );

    StaffDocumentResponse deactivate(
            Long id
    );

    void delete(
            Long id
    );

    StaffDocumentResponse uploadFile(
            Long documentId,
            MultipartFile file
    );

    Resource downloadFile(Long documentId);

    void deleteFile(Long documentId);
}