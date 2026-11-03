package com.smartexam.examsubject.repository;

import com.smartexam.examsubject.entity.ExamSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface ExamSubjectRepository
        extends JpaRepository<ExamSubject, Long>,
        JpaSpecificationExecutor<ExamSubject> {

    List<ExamSubject> findByActiveTrue();

    Page<ExamSubject> findByActiveTrue(
            Pageable pageable
    );

    List<ExamSubject> findByExamIdAndActiveTrue(
            Long examId
    );

    Page<ExamSubject> findByExamIdAndActiveTrue(
            Long examId,
            Pageable pageable
    );

    List<ExamSubject> findBySubjectIdAndActiveTrue(
            Long subjectId
    );

    List<ExamSubject> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    List<ExamSubject> findByExamIdAndExamDateAndActiveTrue(
            Long examId,
            LocalDate examDate
    );

    boolean existsByExamIdAndSubjectId(
            Long examId,
            Long subjectId
    );

    boolean existsByExamIdAndSubjectIdAndIdNot(
            Long examId,
            Long subjectId,
            Long id
    );
}