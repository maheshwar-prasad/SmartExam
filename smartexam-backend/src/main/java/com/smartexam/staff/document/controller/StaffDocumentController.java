package com.smartexam.staff.document.controller;

import com.smartexam.common.exception.BusinessException;
import com.smartexam.staff.document.dto.StaffDocumentRequest;
import com.smartexam.staff.document.dto.StaffDocumentResponse;
import com.smartexam.staff.document.dto.StaffDocumentUpdateRequest;
import com.smartexam.staff.document.enums.DocumentVerificationStatus;
import com.smartexam.staff.document.service.StaffDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import com.smartexam.staff.document.entity.StaffDocument;
import com.smartexam.staff.document.repository.StaffDocumentRepository;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/staff-documents")
@RequiredArgsConstructor
public class StaffDocumentController {

    private final StaffDocumentService documentService;
    private final StaffDocumentRepository staffDocumentRepository;

    @PostMapping
    public ResponseEntity<StaffDocumentResponse> create(
            @Valid @RequestBody StaffDocumentRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffDocumentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StaffDocumentUpdateRequest request
    ) {

        return ResponseEntity.ok(
                documentService.update(id, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDocumentResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                documentService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<StaffDocumentResponse>> getAll() {

        return ResponseEntity.ok(
                documentService.getAll()
        );
    }

    @GetMapping("/page")
    public ResponseEntity<Page<StaffDocumentResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {

        return ResponseEntity.ok(
                documentService.getAll(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<StaffDocumentResponse>>
    getByOrganization(
            @PathVariable Long organizationId
    ) {

        return ResponseEntity.ok(
                documentService.getByOrganization(
                        organizationId
                )
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<StaffDocumentResponse>>
    getByTeacher(
            @PathVariable Long teacherId
    ) {

        return ResponseEntity.ok(
                documentService.getByTeacher(
                        teacherId
                )
        );
    }

    @GetMapping("/organization/{organizationId}/status/{status}")
    public ResponseEntity<List<StaffDocumentResponse>>
    getByStatus(
            @PathVariable Long organizationId,
            @PathVariable DocumentVerificationStatus status
    ) {

        return ResponseEntity.ok(
                documentService.getByStatus(
                        organizationId,
                        status
                )
        );
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<StaffDocumentResponse> verify(
            @PathVariable Long id,
            @RequestParam Long verifiedBy,
            @RequestParam(required = false) String comments
    ) {

        return ResponseEntity.ok(
                documentService.verify(
                        id,
                        verifiedBy,
                        comments
                )
        );
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<StaffDocumentResponse> reject(
            @PathVariable Long id,
            @RequestParam Long rejectedBy,
            @RequestParam(required = false) String comments
    ) {

        return ResponseEntity.ok(
                documentService.reject(
                        id,
                        rejectedBy,
                        comments
                )
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<StaffDocumentResponse> activate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                documentService.activate(id)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<StaffDocumentResponse> deactivate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                documentService.deactivate(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        documentService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/{id}/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<StaffDocumentResponse> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {

        return ResponseEntity.ok(
                documentService.uploadFile(
                        id,
                        file
                )
        );
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long id
    ) {

        StaffDocument document =
                staffDocumentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + id
                                )
                        );

        Resource resource =
                documentService.downloadFile(id);

        MediaType mediaType =
                MediaType.APPLICATION_OCTET_STREAM;

        if (document.getFileType() != null &&
                !document.getFileType().isBlank()) {

            try {
                mediaType =
                        MediaType.parseMediaType(
                                document.getFileType()
                        );
            } catch (Exception ignored) {
                mediaType =
                        MediaType.APPLICATION_OCTET_STREAM;
            }
        }

        String fileName =
                document.getFileName() != null
                        ? document.getFileName()
                        : "staff-document";

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\""
                )
                .body(resource);
    }

    @DeleteMapping("/{id}/file")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long id
    ) {

        documentService.deleteFile(id);

        return ResponseEntity.noContent().build();
    }
}