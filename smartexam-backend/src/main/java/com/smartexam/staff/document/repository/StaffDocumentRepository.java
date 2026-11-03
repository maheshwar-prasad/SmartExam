package com.smartexam.staff.document.repository;

import com.smartexam.staff.document.entity.StaffDocument;
import com.smartexam.staff.document.enums.DocumentVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffDocumentRepository
        extends JpaRepository<StaffDocument, Long> {

    List<StaffDocument> findByOrganizationId(
            Long organizationId
    );

    List<StaffDocument> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    List<StaffDocument> findByTeacherId(
            Long teacherId
    );

    List<StaffDocument> findByTeacherIdAndActiveTrue(
            Long teacherId
    );

    List<StaffDocument> findByTeacherIdAndVerificationStatus(
            Long teacherId,
            DocumentVerificationStatus verificationStatus
    );

    List<StaffDocument> findByOrganizationIdAndVerificationStatus(
            Long organizationId,
            DocumentVerificationStatus verificationStatus
    );

    Optional<StaffDocument> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    boolean existsByTeacherIdAndDocumentTypeIdAndDocumentNumber(
            Long teacherId,
            Long documentTypeId,
            String documentNumber
    );

    boolean existsByTeacherIdAndDocumentTypeIdAndDocumentNumberAndIdNot(
            Long teacherId,
            Long documentTypeId,
            String documentNumber,
            Long id
    );
}