package com.smartexam.institution.campuses.repository;

import com.smartexam.institution.campuses.entity.Campus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampusRepository
        extends JpaRepository<Campus, Long> {

    List<Campus> findByOrganizationId(
            Long organizationId
    );

    Page<Campus> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );

    Page<Campus> findByOrganizationIdAndActive(
            Long organizationId,
            Boolean active,
            Pageable pageable
    );

    Optional<Campus> findByIdAndOrganizationId(
            Long id,
            Long organizationId
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