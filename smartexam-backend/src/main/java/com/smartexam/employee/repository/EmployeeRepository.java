package com.smartexam.employee.repository;

import com.smartexam.employee.entity.Employee;
import com.smartexam.employee.enums.EmployeeType;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByOrganization_IdOrderByFirstNameAsc(
            Long organizationId
    );

    List<Employee> findByOrganization_IdAndEmployeeTypeOrderByFirstNameAsc(
            Long organizationId,
            EmployeeType employeeType
    );

    List<Employee> findByOrganization_IdAndActiveTrueOrderByFirstNameAsc(
            Long organizationId
    );

    Optional<Employee> findByOrganization_IdAndEmployeeCode(
            Long organizationId,
            String employeeCode
    );

    boolean existsByOrganization_IdAndEmployeeCode(
            Long organizationId,
            String employeeCode
    );

    boolean existsByOrganization_IdAndUser_Id(
            Long organizationId, Long userId);

    Optional<Employee> findByOrganization_IdAndUser_Id(
            Long organizationId, Long userId);

    Page<Employee> findByOrganization_Id(
            Long organizationId,
            org.springframework.data.domain.Pageable pageable);

    List<Employee> findByOrganization_Id(Long organizationId);

    long countByOrganization_IdAndActiveTrue(Long organizationId);
}