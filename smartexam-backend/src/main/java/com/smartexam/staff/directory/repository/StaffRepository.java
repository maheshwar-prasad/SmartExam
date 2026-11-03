package com.smartexam.staff.directory.repository;

import com.smartexam.staff.directory.entity.Staff;
import com.smartexam.staff.directory.enums.StaffStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StaffRepository
        extends JpaRepository<Staff, Long> {

    /*
     * Find staff by organization and ID.
     */
    Optional<Staff> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    /*
     * Basic paginated list.
     */
    Page<Staff> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );

    /*
     * Filter by status.
     */
    Page<Staff> findByOrganizationIdAndStatus(
            Long organizationId,
            StaffStatus status,
            Pageable pageable
    );

    /*
     * Filter by active.
     */
    Page<Staff> findByOrganizationIdAndActive(
            Long organizationId,
            Boolean active,
            Pageable pageable
    );

    /*
     * Filter by status + active.
     */
    Page<Staff> findByOrganizationIdAndStatusAndActive(
            Long organizationId,
            StaffStatus status,
            Boolean active,
            Pageable pageable
    );

    /*
     * Search by:
     *
     * staff code
     * employee code
     * first name
     * middle name
     * last name
     * email
     * phone
     * designation
     */
    @Query("""
        SELECT s
        FROM Staff s
        WHERE s.organizationId = :organizationId
          AND (
                LOWER(COALESCE(s.staffCode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.employeeCode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.firstName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.middleName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.lastName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.phone, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.designation, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        """)
    Page<Staff> searchByOrganizationId(
            @Param("organizationId") Long organizationId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /*
     * Search + status.
     */
    @Query("""
        SELECT s
        FROM Staff s
        WHERE s.organizationId = :organizationId
          AND s.status = :status
          AND (
                LOWER(COALESCE(s.staffCode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.employeeCode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.firstName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.middleName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.lastName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.phone, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.designation, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        """)
    Page<Staff> searchByOrganizationIdAndStatus(
            @Param("organizationId") Long organizationId,
            @Param("keyword") String keyword,
            @Param("status") StaffStatus status,
            Pageable pageable
    );

    /*
     * Search + active.
     */
    @Query("""
        SELECT s
        FROM Staff s
        WHERE s.organizationId = :organizationId
          AND s.active = :active
          AND (
                LOWER(COALESCE(s.staffCode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.employeeCode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.firstName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.middleName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.lastName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.phone, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.designation, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        """)
    Page<Staff> searchByOrganizationIdAndActive(
            @Param("organizationId") Long organizationId,
            @Param("keyword") String keyword,
            @Param("active") Boolean active,
            Pageable pageable
    );

    /*
     * Search + status + active.
     */
    @Query("""
        SELECT s
        FROM Staff s
        WHERE s.organizationId = :organizationId
          AND s.status = :status
          AND s.active = :active
          AND (
                LOWER(COALESCE(s.staffCode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.employeeCode, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.firstName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.middleName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.lastName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.phone, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(COALESCE(s.designation, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        """)
    Page<Staff> searchByOrganizationIdAndStatusAndActive(
            @Param("organizationId") Long organizationId,
            @Param("keyword") String keyword,
            @Param("status") StaffStatus status,
            @Param("active") Boolean active,
            Pageable pageable
    );

    /*
     * Duplicate checks - CREATE.
     */
    boolean existsByOrganizationIdAndStaffCode(
            Long organizationId,
            String staffCode
    );

    boolean existsByOrganizationIdAndEmployeeCode(
            Long organizationId,
            String employeeCode
    );

    boolean existsByOrganizationIdAndEmail(
            Long organizationId,
            String email
    );

    /*
     * Duplicate checks - UPDATE.
     *
     * Excludes the current staff record.
     */
    boolean existsByOrganizationIdAndStaffCodeAndIdNot(
            Long organizationId,
            String staffCode,
            Long id
    );

    boolean existsByOrganizationIdAndEmployeeCodeAndIdNot(
            Long organizationId,
            String employeeCode,
            Long id
    );

    boolean existsByOrganizationIdAndEmailAndIdNot(
            Long organizationId,
            String email,
            Long id
    );
}