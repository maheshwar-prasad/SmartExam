package com.smartexam.studentdocument.service.impl;

import com.smartexam.common.exception.DuplicateResourceException;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.student.entity.Student;
import com.smartexam.student.repository.StudentRepository;
import com.smartexam.studentdocument.dto.StudentDocumentResponse;
import com.smartexam.studentdocument.entity.StudentDocument;
import com.smartexam.studentdocument.enums.StudentDocumentType;
import com.smartexam.studentdocument.repository.StudentDocumentRepository;
import com.smartexam.studentdocument.service.StudentDocumentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentDocumentServiceImpl
        implements StudentDocumentService {

    private final StudentDocumentRepository documentRepository;

    private final StudentRepository studentRepository;

    private final CurrentUserService currentUserService;

    @Value("${smartexam.storage.student-documents.location}")
    private String storageLocation;


    // =========================================================
    // Upload
    // =========================================================

    @Override
    public StudentDocumentResponse upload(
            Long studentId,
            StudentDocumentType documentType,
            String documentName,
            String documentNumber,
            String remarks,
            MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Document file is required"
            );
        }

        Long organizationId =
                currentUserService.getOrganizationId();

        Student student =
                studentRepository
                        .findById(studentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id : "
                                                + studentId
                                )
                        );

        if (!organizationId.equals(
                student.getOrganization().getId())) {

            throw new ResourceNotFoundException(
                    "Student not found with id : "
                            + studentId
            );
        }


        // =====================================================
        // Duplicate document type
        // =====================================================

        if (documentRepository
                .existsByStudentIdAndDocumentTypeAndActiveTrue(
                        studentId,
                        documentType)) {

            throw new DuplicateResourceException(
                    "Student document type",
                    documentType.name()
            );
        }


        // =====================================================
        // Validate file
        // =====================================================

        validateFile(file);


        // =====================================================
        // Create student directory
        // =====================================================

        Path studentDirectory =
                Paths.get(storageLocation)
                        .resolve(String.valueOf(studentId))
                        .normalize();

        try {

            Files.createDirectories(
                    studentDirectory
            );

        } catch (IOException e) {

            log.error(
                    "Unable to create document directory for student {}",
                    studentId,
                    e
            );

            throw new IllegalStateException(
                    "Unable to prepare document storage",
                    e
            );
        }


        // =====================================================
        // Generate safe file name
        // =====================================================

        String originalFileName =
                file.getOriginalFilename();

        String extension =
                getExtension(originalFileName);

        String storedFileName =
                UUID.randomUUID()
                        + extension;

        Path targetPath =
                studentDirectory
                        .resolve(storedFileName)
                        .normalize();


        // Security: prevent path traversal

        if (!targetPath.startsWith(
                studentDirectory)) {

            throw new IllegalArgumentException(
                    "Invalid file name"
            );
        }


        // =====================================================
        // Store file
        // =====================================================

        try {

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            log.error(
                    "Unable to store student document",
                    e
            );

            throw new IllegalStateException(
                    "Unable to store document",
                    e
            );
        }


        // =====================================================
        // Save metadata
        // =====================================================

        StudentDocument document =
                StudentDocument.builder()
                        .student(student)
                        .documentType(documentType)
                        .documentName(documentName)
                        .originalFileName(originalFileName)
                        .storedFileName(storedFileName)
                        .storagePath(
                                targetPath.toString()
                        )
                        .contentType(
                                file.getContentType()
                        )
                        .fileSize(
                                file.getSize()
                        )
                        .documentNumber(
                                documentNumber
                        )
                        .remarks(
                                remarks
                        )
                        .active(true)
                        .build();

        StudentDocument saved =
                documentRepository.save(document);

        log.info(
                "Student document uploaded. studentId={}, documentId={}",
                studentId,
                saved.getId()
        );

        return toResponse(saved);
    }


    // =========================================================
    // Get by ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public StudentDocumentResponse getById(
            Long id) {

        StudentDocument document =
                getActiveDocument(id);

        validateOrganization(document);

        return toResponse(document);
    }


    // =========================================================
    // Get by Student
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<StudentDocumentResponse> getByStudentId(
            Long studentId) {

        Long organizationId =
                currentUserService.getOrganizationId();

        Student student =
                studentRepository
                        .findById(studentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id : "
                                                + studentId
                                )
                        );

        if (!organizationId.equals(
                student.getOrganization().getId())) {

            throw new ResourceNotFoundException(
                    "Student not found with id : "
                            + studentId
            );
        }

        return documentRepository
                .findByStudentIdAndActiveTrue(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================================================
    // Deactivate
    // =========================================================

    @Override
    public void deactivate(Long id) {

        StudentDocument document =
                getActiveDocument(id);

        validateOrganization(document);

        document.setActive(false);

        documentRepository.save(document);

        log.info(
                "Student document deactivated. id={}",
                id
        );
    }


    // =========================================================
    // Activate
    // =========================================================

    @Override
    public void activate(Long id) {

        StudentDocument document =
                documentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student document not found with id : "
                                                + id
                                )
                        );

        validateOrganization(document);

        if (Boolean.TRUE.equals(
                document.getActive())) {

            return;
        }

        document.setActive(true);

        documentRepository.save(document);

        log.info(
                "Student document activated. id={}",
                id
        );
    }


    // =========================================================
    // Active document
    // =========================================================

    private StudentDocument getActiveDocument(
            Long id) {

        return documentRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student document not found with id : "
                                        + id
                        )
                );
    }


    // =========================================================
    // Organization validation
    // =========================================================

    private void validateOrganization(
            StudentDocument document) {

        Long organizationId =
                currentUserService.getOrganizationId();

        if (!organizationId.equals(
                document.getStudent()
                        .getOrganization()
                        .getId())) {

            throw new ResourceNotFoundException(
                    "Student document not found with id : "
                            + document.getId()
            );
        }
    }


    // =========================================================
    // File validation
    // =========================================================

    private void validateFile(
            MultipartFile file) {

        String contentType =
                file.getContentType();

        if (contentType == null) {

            throw new IllegalArgumentException(
                    "Unable to determine file type"
            );
        }

        boolean allowed =
                contentType.equals("application/pdf")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("image/png");

        if (!allowed) {

            throw new IllegalArgumentException(
                    "Only PDF, JPG and PNG files are allowed"
            );
        }


        // 10 MB limit

        long maxSize =
                10 * 1024 * 1024;

        if (file.getSize() > maxSize) {

            throw new IllegalArgumentException(
                    "File size must not exceed 10 MB"
            );
        }
    }


    // =========================================================
    // Extension
    // =========================================================

    private String getExtension(
            String fileName) {

        if (fileName == null) {
            return "";
        }

        int index =
                fileName.lastIndexOf('.');

        if (index < 0) {
            return "";
        }

        return fileName.substring(index)
                .toLowerCase();
    }


    // =========================================================
    // Response
    // =========================================================

    private StudentDocumentResponse toResponse(
            StudentDocument document) {

        Student student =
                document.getStudent();

        String studentName =
                student.getFirstName()
                        + " "
                        + (
                        student.getLastName() == null
                                ? ""
                                : student.getLastName()
                );

        return StudentDocumentResponse.builder()
                .id(document.getId())
                .studentId(student.getId())
                .studentName(studentName.trim())
                .studentCode(student.getStudentCode())
                .documentType(document.getDocumentType())
                .documentName(document.getDocumentName())
                .originalFileName(
                        document.getOriginalFileName()
                )
                .contentType(
                        document.getContentType()
                )
                .fileSize(
                        document.getFileSize()
                )
                .documentNumber(
                        document.getDocumentNumber()
                )
                .remarks(
                        document.getRemarks()
                )
                .active(
                        document.getActive()
                )
                .createdAt(
                        document.getCreatedAt() == null
                                ? null
                                : document.getCreatedAt()
                                .toString()
                )
                .updatedAt(
                        document.getUpdatedAt() == null
                                ? null
                                : document.getUpdatedAt()
                                .toString()
                )
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource download(Long id) {

        StudentDocument document =
                getActiveDocument(id);

        validateOrganization(document);

        Path path =
                Paths.get(document.getStoragePath())
                        .normalize();

        if (!Files.exists(path)) {

            throw new ResourceNotFoundException(
                    "Document file not found for id : " + id
            );
        }

        try {

            return new org.springframework.core.io.UrlResource(
                    path.toUri()
            );

        } catch (Exception e) {

            log.error(
                    "Unable to load student document. id={}",
                    id,
                    e
            );

            throw new IllegalStateException(
                    "Unable to load document",
                    e
            );
        }
    }
}