package com.smartexam.curriculum.repository;

import com.smartexam.curriculum.entity.Curriculum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurriculumRepository
        extends JpaRepository<Curriculum, Long> {

    List<Curriculum>
    findByOrganizationId(
            Long organizationId
    );


    List<Curriculum>
    findByOrganizationIdAndActiveTrue(
            Long organizationId
    );


    List<Curriculum>
    findByAcademicSessionId(
            Long academicSessionId
    );


    List<Curriculum>
    findByClassRoomId(
            Long classRoomId
    );


    List<Curriculum>
    findByAcademicSessionIdAndClassRoomId(
            Long academicSessionId,
            Long classRoomId
    );


    List<Curriculum>
    findByOrganizationIdAndAcademicSessionIdAndClassRoomId(
            Long organizationId,
            Long academicSessionId,
            Long classRoomId
    );


    Optional<Curriculum>
    findByIdAndOrganizationId(
            Long id,
            Long organizationId
    );


    boolean existsByOrganizationIdAndAcademicSessionIdAndClassRoomIdAndCode(
            Long organizationId,
            Long academicSessionId,
            Long classRoomId,
            String code
    );


    boolean existsByOrganizationIdAndAcademicSessionIdAndClassRoomIdAndCodeAndIdNot(
            Long organizationId,
            Long academicSessionId,
            Long classRoomId,
            String code,
            Long id
    );
}