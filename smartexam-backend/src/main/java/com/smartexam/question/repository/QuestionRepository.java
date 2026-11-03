package com.smartexam.question.repository;

import com.smartexam.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );

    Optional<Question> findByIdAndOrganizationIdAndActiveTrue(
            Long id,
            Long organizationId
    );

    List<Question> findAllByOrganizationIdAndActiveTrue(
            Long organizationId
    );

    Page<Question> findAllByOrganizationIdAndActiveTrue(
            Long organizationId,
            Pageable pageable
    );

    List<Question> findAllBySubjectIdAndOrganizationIdAndActiveTrue(
            Long subjectId,
            Long organizationId
    );

    long countByOrganizationIdAndActiveTrue(
            Long organizationId
    );
}