package com.smartexam.permission.repository;

import com.smartexam.permission.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long>,
        JpaSpecificationExecutor<Permission> {

    /**
     * Find by Permission Name
     */
    Optional<Permission> findByName(String name);

    /**
     * Duplicate Validation
     */
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(
            String name,
            Long id
    );

    /**
     * Active Permissions
     */
    List<Permission> findByActiveTrue();

    /**
     * Module Wise Permissions
     */
    List<Permission> findByModule(String module);

    /**
     * Operation Wise Permissions
     */
    List<Permission> findByOperation(String operation);

    /**
     * Search
     */
    @Query("""
            SELECT p
            FROM Permission p
            WHERE
                LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(p.module) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(p.operation) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Permission> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**
     * Dashboard Count
     */
    long countByActiveTrue();

}