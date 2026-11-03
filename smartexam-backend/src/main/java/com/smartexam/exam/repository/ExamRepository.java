package com.smartexam.exam.repository;

import com.smartexam.common.enums.ExamStatus;
import com.smartexam.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    boolean existsByNameIgnoreCaseAndAcademicSessionIdAndClassRoomIdAndSectionId(
            String name,
            Long academicSessionId,
            Long classRoomId,
            Long sectionId
    );

    boolean existsByNameIgnoreCaseAndAcademicSessionIdAndClassRoomIdAndSectionIdAndIdNot(
            String name,
            Long academicSessionId,
            Long classRoomId,
            Long sectionId,
            Long id
    );

    List<Exam> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    List<Exam> findByAcademicSessionIdAndActiveTrue(
            Long academicSessionId
    );

    List<Exam> findByAcademicSessionIdAndClassRoomIdAndSectionIdAndActiveTrue(
            Long academicSessionId,
            Long classRoomId,
            Long sectionId
    );

    List<Exam> findByOrganizationIdAndAcademicSessionIdAndActiveTrue(
            Long organizationId,
            Long academicSessionId
    );

    long countByActiveTrue();

    long countByActiveTrueAndStatus(ExamStatus examStatus);

    long countByActiveTrueAndStartDateAfter(LocalDate date);
}