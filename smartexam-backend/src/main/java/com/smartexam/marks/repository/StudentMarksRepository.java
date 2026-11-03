package com.smartexam.marks.repository;

import com.smartexam.marks.entity.StudentMarks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StudentMarksRepository
        extends JpaRepository<StudentMarks, Long>,
        JpaSpecificationExecutor<StudentMarks> {

    /**
     * Get all active marks.
     */
    List<StudentMarks> findByActiveTrue();

    /**
     * Get paginated active marks.
     */
    Page<StudentMarks> findByActiveTrue(
            Pageable pageable
    );

    /**
     * Get marks by student.
     */
    List<StudentMarks> findByStudentIdAndActiveTrue(
            Long studentId
    );

    /**
     * Get marks by exam.
     */
    List<StudentMarks> findByExamIdAndActiveTrue(
            Long examId
    );

    /**
     * Get marks by exam subject.
     */
    List<StudentMarks> findByExamSubjectIdAndActiveTrue(
            Long examSubjectId
    );

    /**
     * Get marks by organization.
     */
    List<StudentMarks> findByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    /**
     * Check duplicate marks for the same student
     * and exam subject during create.
     */
    boolean existsByStudentIdAndExamSubjectId(
            Long studentId,
            Long examSubjectId
    );

    /**
     * Check duplicate marks during update,
     * excluding the current record.
     */
    boolean existsByStudentIdAndExamSubjectIdAndIdNot(
            Long studentId,
            Long examSubjectId,
            Long id
    );

    List<StudentMarks> findByStudentIdAndExamIdAndActiveTrue(
            Long studentId,
            Long examId
    );
}