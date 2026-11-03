package com.smartexam.examschedule.repository;

import com.smartexam.examschedule.entity.ExamSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ExamScheduleRepository
        extends JpaRepository<ExamSchedule, Long>,
        JpaSpecificationExecutor<ExamSchedule> {

    List<ExamSchedule> findByActiveTrue();

    Page<ExamSchedule> findByActiveTrue(
            Pageable pageable
    );

    List<ExamSchedule> findByExamIdAndActiveTrue(
            Long examId
    );

    List<ExamSchedule> findByExamSubjectIdAndActiveTrue(
            Long examSubjectId
    );

    List<ExamSchedule> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    List<ExamSchedule> findByExamIdAndExamDateAndActiveTrue(
            Long examId,
            LocalDate examDate
    );

    boolean existsByExamIdAndExamSubjectId(
            Long examId,
            Long examSubjectId
    );

    boolean existsByExamIdAndExamSubjectIdAndIdNot(
            Long examId,
            Long examSubjectId,
            Long id
    );
}