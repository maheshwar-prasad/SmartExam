package com.smartexam.classsubject.repository;

import com.smartexam.classsubject.entity.ClassSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClassSubjectRepository
        extends JpaRepository<ClassSubject, Long>,
        JpaSpecificationExecutor<ClassSubject> {

    boolean existsByOrganizationIdAndAcademicSessionIdAndClassRoomIdAndSubjectId(
            Long organizationId,
            Long academicSessionId,
            Long classRoomId,
            Long subjectId
    );

    boolean existsByOrganizationIdAndAcademicSessionIdAndClassRoomIdAndSubjectIdAndIdNot(
            Long organizationId,
            Long academicSessionId,
            Long classRoomId,
            Long subjectId,
            Long id
    );
}