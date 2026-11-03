package com.smartexam.section.repository;

import com.smartexam.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends
        JpaRepository<Section, Long>,
        JpaSpecificationExecutor<Section> {

    /**
     * Check duplicate section code within an organization.
     */
    boolean existsByCodeIgnoreCaseAndOrganizationId(
            String code,
            Long organizationId);

    /**
     * Check duplicate section name within a classroom.
     */
    boolean existsByNameIgnoreCaseAndClassRoomId(
            String name,
            Long classRoomId);

    /**
     * Duplicate section code while updating.
     */
    boolean existsByCodeIgnoreCaseAndOrganizationIdAndIdNot(
            String code,
            Long organizationId,
            Long id);

    /**
     * Duplicate section name while updating.
     */
    boolean existsByNameIgnoreCaseAndClassRoomIdAndIdNot(
            String name,
            Long classRoomId,
            Long id);

    /**
     * Find all sections by organization.
     */
    List<Section> findByOrganizationId(Long organizationId);

    /**
     * Find all sections by academic session.
     */
    List<Section> findByAcademicSessionId(Long academicSessionId);

    /**
     * Find all sections by classroom.
     */
    List<Section> findByClassRoomId(Long classRoomId);

    /**
     * Find all active sections.
     */
    List<Section> findByActiveTrue();

    /**
     * Find by code.
     */
    Optional<Section> findByCodeIgnoreCase(String code);

    long countByActiveTrue();
}