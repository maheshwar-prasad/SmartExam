package com.smartexam.academic.repository;

import com.smartexam.academic.entity.AcademicSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AcademicSessionRepository extends
        JpaRepository<AcademicSession, Long>,
        JpaSpecificationExecutor<AcademicSession> {

    /**
     * Find by Code
     */
    Optional<AcademicSession> findByCode(String code);

    /**
     * Duplicate Validation
     */
    boolean existsByCodeIgnoreCaseAndOrganizationId(
            String code,
            Long organizationId
    );

    boolean existsByCodeIgnoreCaseAndOrganizationIdAndIdNot(
            String code,
            Long organizationId,
            Long id
    );

    /**
     * Current Session
     */
    Optional<AcademicSession> findByOrganizationIdAndCurrentSessionTrue(
            Long organizationId
    );

    /**
     * Organization Sessions
     */
    Page<AcademicSession> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );

    /**
     * Active Sessions
     */
    List<AcademicSession> findByActiveTrue();

    /**
     * Search
     */
    @Query("""
            SELECT a
            FROM AcademicSession a
            WHERE
                LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<AcademicSession> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**
     * Dashboard Count
     */
    long countByActiveTrue();

    long countByOrganizationId(Long organizationId);

    /**
     * Find Sessions Overlapping Date Range
     */
    @Query("""
            SELECT a
            FROM AcademicSession a
            WHERE a.organization.id = :organizationId
            AND (
                :startDate BETWEEN a.startDate AND a.endDate
                OR
                :endDate BETWEEN a.startDate AND a.endDate
                OR
                a.startDate BETWEEN :startDate AND :endDate
            )
            """)
    List<AcademicSession> findOverlappingSessions(
            @Param("organizationId") Long organizationId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    @Query("""
        SELECT a
        FROM AcademicSession a
        WHERE a.organization.id = :organizationId
        AND a.id <> :id
        AND (
                :startDate BETWEEN a.startDate AND a.endDate
                OR
                :endDate BETWEEN a.startDate AND a.endDate
                OR
                a.startDate BETWEEN :startDate AND :endDate
        )
        """)
            List<AcademicSession> findOverlappingSessionsForUpdate(
                    @Param("id") Long id,
                    @Param("organizationId") Long organizationId,
                    @Param("startDate") LocalDate startDate,
                    @Param("endDate") LocalDate endDate
            );
}