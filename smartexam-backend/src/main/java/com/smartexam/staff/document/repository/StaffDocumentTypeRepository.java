package com.smartexam.staff.document.repository;

import com.smartexam.staff.document.entity.StaffDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffDocumentTypeRepository
        extends JpaRepository<StaffDocumentType, Long> {

    List<StaffDocumentType> findByOrganizationId(
            Long organizationId
    );

    List<StaffDocumentType> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    Optional<StaffDocumentType> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    Optional<StaffDocumentType> findByOrganizationIdAndCode(
            Long organizationId,
            String code
    );

    boolean existsByOrganizationIdAndCode(
            Long organizationId,
            String code
    );

    boolean existsByOrganizationIdAndCodeAndIdNot(
            Long organizationId,
            String code,
            Long id
    );
}