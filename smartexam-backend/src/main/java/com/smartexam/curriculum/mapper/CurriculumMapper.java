package com.smartexam.curriculum.mapper;

import com.smartexam.curriculum.dto.CurriculumResponse;
import com.smartexam.curriculum.dto.CurriculumSubjectResponse;
import com.smartexam.curriculum.entity.Curriculum;
import com.smartexam.curriculum.entity.CurriculumSubject;

import org.springframework.stereotype.Component;

@Component
public class CurriculumMapper {

    public CurriculumResponse toResponse(
            Curriculum curriculum
    ) {

        return new CurriculumResponse(

                curriculum.getId(),

                curriculum.getOrganization() != null
                        ? curriculum.getOrganization().getId()
                        : null,

                curriculum.getOrganization() != null
                        ? curriculum.getOrganization().getName()
                        : null,

                curriculum.getAcademicSession() != null
                        ? curriculum.getAcademicSession().getId()
                        : null,

                curriculum.getAcademicSession() != null
                        ? curriculum.getAcademicSession().getName()
                        : null,

                curriculum.getClassRoom() != null
                        ? curriculum.getClassRoom().getId()
                        : null,

                curriculum.getClassRoom() != null
                        ? curriculum.getClassRoom().getName()
                        : null,

                curriculum.getClassRoom() != null
                        ? curriculum.getClassRoom().getCode()
                        : null,

                curriculum.getName(),

                curriculum.getCode(),

                curriculum.getDescription(),

                curriculum.getActive(),

                curriculum.getCreatedAt(),

                curriculum.getCreatedBy(),

                curriculum.getUpdatedAt(),

                curriculum.getUpdatedBy()
        );
    }


    public CurriculumSubjectResponse
    toSubjectResponse(
            CurriculumSubject curriculumSubject
    ) {

        return new CurriculumSubjectResponse(

                curriculumSubject.getId(),

                curriculumSubject.getCurriculum() != null
                        ? curriculumSubject.getCurriculum().getId()
                        : null,

                curriculumSubject.getClassSubject() != null
                        ? curriculumSubject.getClassSubject().getId()
                        : null,

                curriculumSubject.getClassSubject() != null &&
                        curriculumSubject.getClassSubject().getSubject() != null
                        ? curriculumSubject
                        .getClassSubject()
                        .getSubject()
                        .getId()
                        : null,

                curriculumSubject.getClassSubject() != null &&
                        curriculumSubject.getClassSubject().getSubject() != null
                        ? curriculumSubject
                        .getClassSubject()
                        .getSubject()
                        .getName()
                        : null,

                curriculumSubject.getClassSubject() != null &&
                        curriculumSubject.getClassSubject().getSubject() != null
                        ? curriculumSubject
                        .getClassSubject()
                        .getSubject()
                        .getCode()
                        : null,

                curriculumSubject.getSequenceNo(),

                curriculumSubject.getTheoryMarks(),

                curriculumSubject.getPracticalMarks(),

                curriculumSubject.getPassingMarks(),

                curriculumSubject.getActive(),

                curriculumSubject.getCreatedAt(),

                curriculumSubject.getCreatedBy(),

                curriculumSubject.getUpdatedAt(),

                curriculumSubject.getUpdatedBy()
        );
    }
}