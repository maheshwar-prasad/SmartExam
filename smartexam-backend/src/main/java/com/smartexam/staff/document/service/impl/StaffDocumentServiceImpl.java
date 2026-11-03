package com.smartexam.staff.document.service.impl;

import com.smartexam.common.exception.BusinessException;
import com.smartexam.organization.entity.Organization;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.staff.document.dto.StaffDocumentRequest;
import com.smartexam.staff.document.dto.StaffDocumentResponse;
import com.smartexam.staff.document.dto.StaffDocumentUpdateRequest;
import com.smartexam.staff.document.entity.StaffDocument;
import com.smartexam.staff.document.entity.StaffDocumentType;
import com.smartexam.staff.document.enums.DocumentVerificationStatus;
import com.smartexam.staff.document.mapper.StaffDocumentMapper;
import com.smartexam.staff.document.repository.StaffDocumentRepository;
import com.smartexam.staff.document.repository.StaffDocumentTypeRepository;
import com.smartexam.staff.document.service.StaffDocumentService;
import com.smartexam.staff.document.storage.StaffDocumentStorageService;
import com.smartexam.teacher.entity.Teacher;
import com.smartexam.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StaffDocumentServiceImpl
        implements StaffDocumentService {

    private final StaffDocumentRepository documentRepository;
    private final StaffDocumentTypeRepository documentTypeRepository;
    private final StaffDocumentMapper documentMapper;
    private final OrganizationRepository organizationRepository;
    private final TeacherRepository teacherRepository;
    private final StaffDocumentStorageService staffDocumentStorageService;

    @Override
    public StaffDocumentResponse create(
            StaffDocumentRequest request
    ) {

        if (request == null) {
            throw new BusinessException(
                    "Staff document request is required"
            );
        }

        Organization organization =
                organizationRepository
                        .findById(request.organizationId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Organization not found: "
                                                + request.organizationId()
                                )
                        );

        Teacher teacher =
                teacherRepository
                        .findById(request.teacherId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Teacher not found: "
                                                + request.teacherId()
                                )
                        );

        StaffDocumentType documentType =
                documentTypeRepository
                        .findById(request.documentTypeId())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Document type not found: "
                                                + request.documentTypeId()
                                )
                        );

        validateOrganizationRelationships(
                organization,
                teacher,
                documentType
        );

        validateDates(
                request.issueDate(),
                request.expiryDate()
        );

        if (request.documentNumber() != null
                && !request.documentNumber().isBlank()
                && documentRepository
                .existsByTeacherIdAndDocumentTypeIdAndDocumentNumber(
                        request.teacherId(),
                        request.documentTypeId(),
                        request.documentNumber().trim()
                )) {

            throw new BusinessException(
                    "Document with the same number already exists for this teacher and document type."
            );
        }

        StaffDocument entity =
                documentMapper.toEntity(request);

        entity.setOrganization(organization);
        entity.setTeacher(teacher);
        entity.setDocumentType(documentType);

        entity.setDocumentName(
                request.documentName().trim()
        );

        if (request.documentNumber() != null) {
            entity.setDocumentNumber(
                    request.documentNumber().trim()
            );
        }

        entity.setVerificationStatus(
                DocumentVerificationStatus.PENDING
        );

        entity.setVerifiedBy(null);
        entity.setVerifiedAt(null);
        entity.setVerificationComments(null);

        if (entity.getActive() == null) {
            entity.setActive(true);
        }

        return documentMapper.toResponse(
                documentRepository.save(entity)
        );
    }

    @Override
    public StaffDocumentResponse update(
            Long id,
            StaffDocumentUpdateRequest request
    ) {

        StaffDocument entity =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + id
                                )
                        );

        Organization organization =
                entity.getOrganization();

        if (request.organizationId() != null) {

            organization =
                    organizationRepository
                            .findById(
                                    request.organizationId()
                            )
                            .orElseThrow(() ->
                                    new BusinessException(
                                            "Organization not found: "
                                                    + request.organizationId()
                                    )
                            );
        }

        Teacher teacher =
                entity.getTeacher();

        if (request.teacherId() != null) {

            teacher =
                    teacherRepository
                            .findById(
                                    request.teacherId()
                            )
                            .orElseThrow(() ->
                                    new BusinessException(
                                            "Teacher not found: "
                                                    + request.teacherId()
                                    )
                            );
        }

        StaffDocumentType documentType =
                entity.getDocumentType();

        if (request.documentTypeId() != null) {

            documentType =
                    documentTypeRepository
                            .findById(
                                    request.documentTypeId()
                            )
                            .orElseThrow(() ->
                                    new BusinessException(
                                            "Document type not found: "
                                                    + request.documentTypeId()
                                    )
                            );
        }

        validateOrganizationRelationships(
                organization,
                teacher,
                documentType
        );

        validateDates(
                request.issueDate(),
                request.expiryDate()
        );

        if (request.documentNumber() != null
                && !request.documentNumber().isBlank()) {

            String documentNumber =
                    request.documentNumber().trim();

            if (documentRepository
                    .existsByTeacherIdAndDocumentTypeIdAndDocumentNumberAndIdNot(
                            teacher.getId(),
                            documentType.getId(),
                            documentNumber,
                            id
                    )) {

                throw new BusinessException(
                        "Document with the same number already exists for this teacher and document type."
                );
            }
        }

        documentMapper.updateEntity(
                request,
                entity
        );

        entity.setOrganization(organization);
        entity.setTeacher(teacher);
        entity.setDocumentType(documentType);

        if (request.documentName() != null) {
            entity.setDocumentName(
                    request.documentName().trim()
            );
        }

        if (request.documentNumber() != null) {
            entity.setDocumentNumber(
                    request.documentNumber().trim()
            );
        }

        /*
         * If document metadata changes after verification,
         * require verification again.
         */
        entity.setVerificationStatus(
                DocumentVerificationStatus.PENDING
        );

        entity.setVerifiedBy(null);
        entity.setVerifiedAt(null);
        entity.setVerificationComments(null);

        return documentMapper.toResponse(
                documentRepository.save(entity)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public StaffDocumentResponse getById(
            Long id
    ) {

        StaffDocument entity =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + id
                                )
                        );

        return documentMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDocumentResponse> getAll() {

        return documentMapper.toResponseList(
                documentRepository.findAll()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffDocumentResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort.Direction sortDirection =
                "ASC".equalsIgnoreCase(direction)
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                sortDirection,
                                sortBy
                        )
                );

        return documentRepository
                .findAll(pageable)
                .map(documentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDocumentResponse> getByOrganization(
            Long organizationId
    ) {

        return documentMapper.toResponseList(
                documentRepository
                        .findByOrganizationId(
                                organizationId
                        )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDocumentResponse> getByTeacher(
            Long teacherId
    ) {

        return documentMapper.toResponseList(
                documentRepository
                        .findByTeacherId(
                                teacherId
                        )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDocumentResponse> getByStatus(
            Long organizationId,
            DocumentVerificationStatus status
    ) {

        return documentMapper.toResponseList(
                documentRepository
                        .findByOrganizationIdAndVerificationStatus(
                                organizationId,
                                status
                        )
        );
    }

    @Override
    public StaffDocumentResponse verify(
            Long id,
            Long verifiedBy,
            String comments
    ) {

        StaffDocument entity =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + id
                                )
                        );

        if (!entity.getActive()) {
            throw new BusinessException(
                    "Inactive document cannot be verified."
            );
        }

        if (entity.getVerificationStatus()
                == DocumentVerificationStatus.VERIFIED) {

            throw new BusinessException(
                    "Document is already verified."
            );
        }

        entity.setVerificationStatus(
                DocumentVerificationStatus.VERIFIED
        );

        entity.setVerifiedBy(verifiedBy);
        entity.setVerifiedAt(
                LocalDateTime.now()
        );

        entity.setVerificationComments(
                comments
        );

        return documentMapper.toResponse(
                documentRepository.save(entity)
        );
    }

    @Override
    public StaffDocumentResponse reject(
            Long id,
            Long rejectedBy,
            String comments
    ) {

        StaffDocument entity =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + id
                                )
                        );

        if (!entity.getActive()) {
            throw new BusinessException(
                    "Inactive document cannot be rejected."
            );
        }

        entity.setVerificationStatus(
                DocumentVerificationStatus.REJECTED
        );

        entity.setVerifiedBy(rejectedBy);
        entity.setVerifiedAt(
                LocalDateTime.now()
        );

        entity.setVerificationComments(
                comments
        );

        return documentMapper.toResponse(
                documentRepository.save(entity)
        );
    }

    @Override
    public StaffDocumentResponse activate(
            Long id
    ) {

        StaffDocument entity =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + id
                                )
                        );

        entity.setActive(true);

        return documentMapper.toResponse(
                documentRepository.save(entity)
        );
    }

    @Override
    public StaffDocumentResponse deactivate(
            Long id
    ) {

        StaffDocument entity =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + id
                                )
                        );

        entity.setActive(false);

        return documentMapper.toResponse(
                documentRepository.save(entity)
        );
    }

    @Override
    public void delete(Long id) {

        StaffDocument entity =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + id
                                )
                        );

        documentRepository.delete(entity);
    }

    private void validateOrganizationRelationships(
            Organization organization,
            Teacher teacher,
            StaffDocumentType documentType
    ) {

        Long organizationId =
                organization.getId();

        if (teacher.getOrganization() == null
                || !organizationId.equals(
                teacher.getOrganization().getId()
        )) {

            throw new BusinessException(
                    "Teacher does not belong to Organization."
            );
        }

        if (documentType.getOrganization() == null
                || !organizationId.equals(
                documentType
                        .getOrganization()
                        .getId()
        )) {

            throw new BusinessException(
                    "Document Type does not belong to Organization."
            );
        }
    }

    private void validateDates(
            java.time.LocalDate issueDate,
            java.time.LocalDate expiryDate
    ) {

        if (issueDate != null
                && expiryDate != null
                && expiryDate.isBefore(issueDate)) {

            throw new BusinessException(
                    "Expiry date cannot be before issue date."
            );
        }
    }
    @Override
    @Transactional
    public StaffDocumentResponse uploadFile(
            Long documentId,
            MultipartFile file
    ) {

        StaffDocument document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: "
                                                + documentId
                                )
                        );


        /*
         * Store the new file first.
         */
        StaffDocumentStorageService.StoredFile storedFile =
                staffDocumentStorageService.store(file);


        /*
         * Delete the old physical file only
         * after the new file has been successfully stored.
         */
        String oldStoredFileName =
                document.getStoredFileName();


        /*
         * Update database information.
         */
        document.setFileName(
                storedFile.originalFileName()
        );

        document.setStoredFileName(
                storedFile.storedFileName()
        );

        document.setFilePath(
                storedFile.filePath()
        );

        document.setFileType(
                storedFile.contentType()
        );

        document.setFileSize(
                storedFile.fileSize()
        );


        StaffDocument savedDocument =
                documentRepository.save(
                        document
                );


        /*
         * Delete old file after successful database save.
         */
        if (oldStoredFileName != null &&
                !oldStoredFileName.isBlank() &&
                !oldStoredFileName.equals(
                        storedFile.storedFileName()
                )) {

            try {

                staffDocumentStorageService.delete(
                        oldStoredFileName
                );

            } catch (Exception ex) {

                /*
                 * Do not fail the upload because the old
                 * physical file could not be deleted.
                 */
                log.warn(
                        "Unable to delete old staff document file: {}",
                        oldStoredFileName,
                        ex
                );
            }
        }
        return documentMapper.toResponse(
                savedDocument
        );
    }
    @Override
    @Transactional(readOnly = true)
    public Resource downloadFile(Long documentId) {

        StaffDocument document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: " + documentId
                                )
                        );

        if (document.getStoredFileName() == null ||
                document.getStoredFileName().isBlank()) {

            throw new BusinessException(
                    "No file has been uploaded for staff document: "
                            + documentId
            );
        }

        return staffDocumentStorageService.loadAsResource(
                document.getStoredFileName()
        );
    }

    @Override
    @Transactional
    public void deleteFile(Long documentId) {

        StaffDocument document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Staff document not found: "
                                                + documentId
                                )
                        );

        String storedFileName =
                document.getStoredFileName();

        if (storedFileName == null ||
                storedFileName.isBlank()) {
            return;
        }

        staffDocumentStorageService.delete(storedFileName);

        document.setFileName(null);
        document.setStoredFileName(null);
        document.setFilePath(null);
        document.setFileType(null);
        document.setFileSize(null);

        documentRepository.save(document);
    }
}