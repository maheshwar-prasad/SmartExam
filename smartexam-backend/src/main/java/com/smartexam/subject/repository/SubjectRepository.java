package com.smartexam.subject.repository;

import com.smartexam.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SubjectRepository extends
        JpaRepository<Subject, Long>,
        JpaSpecificationExecutor<Subject> {

    boolean existsByCodeIgnoreCaseAndOrganizationId(
            String code,
            Long organizationId);

    boolean existsByCodeIgnoreCaseAndOrganizationIdAndIdNot(
            String code,
            Long organizationId,
            Long id);

    boolean existsByNameIgnoreCaseAndOrganizationIdAndAcademicSessionId(
            String name,
            Long organizationId,
            Long academicSessionId);

    boolean existsByNameIgnoreCaseAndOrganizationIdAndAcademicSessionIdAndIdNot(
            String name,
            Long organizationId,
            Long academicSessionId,
            Long id);

    Optional<Subject> findByIdAndActiveTrue(Long id);

    long countByActiveTrue();
    Optional<Subject> findByIdAndOrganizationIdAndActiveTrue(
            Long id,
            Long organizationId
    );


}