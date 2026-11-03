
package com.smartexam.institution.department.repository;

import com.smartexam.institution.department.entity.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {


    Optional<Department> findByIdAndActiveTrue(
            Long id
    );


    boolean existsByOrganizationIdAndCodeIgnoreCase(
            Long organizationId,
            String code
    );


    boolean existsByOrganizationIdAndCodeIgnoreCaseAndIdNot(
            Long organizationId,
            String code,
            Long id
    );


    Page<Department> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );


    Page<Department> findByOrganizationIdAndActiveTrue(
            Long organizationId,
            Pageable pageable
    );


    Page<Department> findByOrganizationIdAndNameContainingIgnoreCase(
            Long organizationId,
            String name,
            Pageable pageable
    );


    Page<Department> findByOrganizationIdAndCodeContainingIgnoreCase(
            Long organizationId,
            String code,
            Pageable pageable
    );

}