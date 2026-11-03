package com.smartexam.teacher.repository;

import com.smartexam.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TeacherRepository extends
        JpaRepository<Teacher, Long>,
        JpaSpecificationExecutor<Teacher> {

    boolean existsByEmployeeCodeIgnoreCaseAndOrganizationId(
            String employeeCode,
            Long organizationId);

    boolean existsByEmployeeCodeIgnoreCaseAndOrganizationIdAndIdNot(
            String employeeCode,
            Long organizationId,
            Long id);

    boolean existsByEmailIgnoreCaseAndOrganizationId(
            String email,
            Long organizationId);

    boolean existsByEmailIgnoreCaseAndOrganizationIdAndIdNot(
            String email,
            Long organizationId,
            Long id);

    boolean existsByMobileAndOrganizationId(
            String mobile,
            Long organizationId);

    boolean existsByMobileAndOrganizationIdAndIdNot(
            String mobile,
            Long organizationId,
            Long id);

    boolean existsByUserId(Long userId);

    boolean existsByUserIdAndIdNot(
            Long userId,
            Long id);

    Optional<Teacher> findByIdAndActiveTrue(
            Long id);

    long countByActiveTrue();

}