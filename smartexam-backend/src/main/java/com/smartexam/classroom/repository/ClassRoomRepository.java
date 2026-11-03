package com.smartexam.classroom.repository;

import com.smartexam.classroom.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends
        JpaRepository<ClassRoom, Long>,
        JpaSpecificationExecutor<ClassRoom> {

    /**
     * Check duplicate class code within an organization.
     */
    boolean existsByCodeIgnoreCaseAndOrganizationId(
            String code,
            Long organizationId);

    /**
     * Check duplicate class name within an organization.
     */
    boolean existsByNameIgnoreCaseAndOrganizationId(
            String name,
            Long organizationId);

    /**
     * Duplicate class code while updating.
     */
    boolean existsByCodeIgnoreCaseAndOrganizationIdAndIdNot(
            String code,
            Long organizationId,
            Long id);

    /**
     * Duplicate class name while updating.
     */
    boolean existsByNameIgnoreCaseAndOrganizationIdAndIdNot(
            String name,
            Long organizationId,
            Long id);

    /**
     * Find by Organization.
     */
    List<ClassRoom> findByOrganizationId(Long organizationId);

    /**
     * Find by Academic Session.
     */
    List<ClassRoom> findByAcademicSessionId(Long academicSessionId);

    /**
     * Active ClassRooms.
     */
    List<ClassRoom> findByActiveTrue();

    /**
     * Find by Code.
     */
    Optional<ClassRoom> findByCodeIgnoreCase(String code);

    long countByActiveTrue();
}