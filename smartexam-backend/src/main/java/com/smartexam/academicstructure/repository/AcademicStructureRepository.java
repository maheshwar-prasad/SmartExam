package com.smartexam.academicstructure.repository;

import com.smartexam.academicstructure.entity.AcademicStructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicStructureRepository
        extends JpaRepository<AcademicStructure, Long> {

    List<AcademicStructure>
    findByOrganization_Id(Long organizationId);

    List<AcademicStructure>
    findByAcademicSession_Id(Long academicSessionId);

    List<AcademicStructure>
    findByClassRoom_Id(Long classRoomId);

    List<AcademicStructure>
    findBySection_Id(Long sectionId);

    List<AcademicStructure>
    findByAcademicSession_IdAndClassRoom_Id(
            Long academicSessionId,
            Long classRoomId
    );

    List<AcademicStructure>
    findByOrganization_IdAndActiveTrue(
            Long organizationId
    );

    boolean existsByOrganization_IdAndAcademicSession_IdAndCode(
            Long organizationId,
            Long academicSessionId,
            String code
    );

    boolean existsByOrganization_IdAndAcademicSession_IdAndCodeAndIdNot(
            Long organizationId,
            Long academicSessionId,
            String code,
            Long id
    );
}