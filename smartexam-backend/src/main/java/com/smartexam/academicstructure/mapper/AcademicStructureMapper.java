package com.smartexam.academicstructure.mapper;

import com.smartexam.academicstructure.dto.AcademicStructureResponse;
import com.smartexam.academicstructure.entity.AcademicStructure;

import org.springframework.stereotype.Component;

@Component
public class AcademicStructureMapper {

    public AcademicStructureResponse toResponse(
            AcademicStructure entity) {

        return new AcademicStructureResponse(

                entity.getId(),

                entity.getOrganization() != null
                        ? entity.getOrganization().getId()
                        : null,

                entity.getOrganization() != null
                        ? entity.getOrganization().getName()
                        : null,

                entity.getAcademicSession() != null
                        ? entity.getAcademicSession().getId()
                        : null,

                entity.getAcademicSession() != null
                        ? entity.getAcademicSession().getName()
                        : null,

                entity.getClassRoom() != null
                        ? entity.getClassRoom().getId()
                        : null,

                entity.getClassRoom() != null
                        ? entity.getClassRoom().getName()
                        : null,

                entity.getClassRoom() != null
                        ? entity.getClassRoom().getCode()
                        : null,

                entity.getSection() != null
                        ? entity.getSection().getId()
                        : null,

                entity.getSection() != null
                        ? entity.getSection().getName()
                        : null,

                entity.getSection() != null
                        ? entity.getSection().getCode()
                        : null,

                entity.getName(),

                entity.getCode(),

                entity.getDescription(),

                entity.getActive(),

                entity.getCreatedAt(),

                entity.getCreatedBy(),

                entity.getUpdatedAt(),

                entity.getUpdatedBy()
        );
    }
}