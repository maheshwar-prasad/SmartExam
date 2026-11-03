package com.smartexam.studentdocument.repository;

import com.smartexam.studentdocument.entity.StudentDocument;
import com.smartexam.studentdocument.enums.StudentDocumentType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentDocumentRepository
        extends JpaRepository<StudentDocument, Long> {

    List<StudentDocument> findByStudentIdAndActiveTrue(
            Long studentId
    );

    Optional<StudentDocument>
    findByIdAndActiveTrue(Long id);

    List<StudentDocument>
    findByStudentIdAndDocumentTypeAndActiveTrue(
            Long studentId,
            StudentDocumentType documentType
    );

    boolean existsByStudentIdAndDocumentTypeAndActiveTrue(
            Long studentId,
            StudentDocumentType documentType
    );
}