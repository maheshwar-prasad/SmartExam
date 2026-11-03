package com.smartexam.parentguardian.repository;

import com.smartexam.parentguardian.entity.StudentParentGuardian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentParentGuardianRepository
        extends JpaRepository<StudentParentGuardian, Long> {

    List<StudentParentGuardian> findByStudentIdAndActiveTrue(
            Long studentId
    );

    List<StudentParentGuardian> findByParentGuardianIdAndActiveTrue(
            Long parentGuardianId
    );

    Optional<StudentParentGuardian>
    findByStudentIdAndParentGuardianIdAndRelation(
            Long studentId,
            Long parentGuardianId,
            com.smartexam.parentguardian.enums.ParentGuardianRelation relation
    );

    boolean existsByStudentIdAndParentGuardianIdAndRelation(
            Long studentId,
            Long parentGuardianId,
            com.smartexam.parentguardian.enums.ParentGuardianRelation relation
    );

    boolean existsByStudentIdAndRelationAndPrimaryContactTrue(
            Long studentId,
            com.smartexam.parentguardian.enums.ParentGuardianRelation relation
    );
}