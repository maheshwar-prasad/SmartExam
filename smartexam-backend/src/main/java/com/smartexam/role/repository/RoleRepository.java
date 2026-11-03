package com.smartexam.role.repository;

import com.smartexam.role.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>,
        JpaSpecificationExecutor<Role> {

    /**
     * Used by Authentication
     */
    @Query("""
            SELECT DISTINCT r
            FROM Role r
            LEFT JOIN FETCH r.permissions
            WHERE r.name = :name
            """)
    Optional<Role> findByName(@Param("name") String name);

    /**
     * Duplicate Validation
     */
    boolean existsByName(String name);

    /**
     * Active Roles
     */
    List<Role> findByActiveTrue();

    /**
     * Active Roles Pagination
     */
    Page<Role> findByActiveTrue(Pageable pageable);

    /**
     * Search Roles
     */
    @Query("""
            SELECT r
            FROM Role r
            WHERE
            LOWER(r.name)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR
            LOWER(r.description)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Role> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**
     * Count Active Roles
     */
    long countByActiveTrue();

}