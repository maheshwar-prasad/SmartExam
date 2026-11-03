package com.smartexam.syllabus.mapper;

import com.smartexam.syllabus.dto.SyllabusResponse;
import com.smartexam.syllabus.entity.Syllabus;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMapper {


    public SyllabusResponse toResponse(
            Syllabus syllabus
    ) {

        String organizationName = null;

        if (syllabus.getOrganization() != null) {
            organizationName =
                    syllabus.getOrganization().getName();
        }


        String curriculumName = null;
        String curriculumCode = null;

        if (syllabus.getCurriculum() != null) {

            curriculumName =
                    syllabus.getCurriculum().getName();

            curriculumCode =
                    syllabus.getCurriculum().getCode();
        }


        Long subjectId = null;
        String subjectName = null;
        String subjectCode = null;

        if (
                syllabus.getClassSubject() != null &&
                        syllabus.getClassSubject().getSubject() != null
        ) {

            subjectId =
                    syllabus.getClassSubject()
                            .getSubject()
                            .getId();

            subjectName =
                    syllabus.getClassSubject()
                            .getSubject()
                            .getName();

            subjectCode =
                    syllabus.getClassSubject()
                            .getSubject()
                            .getCode();
        }


        return new SyllabusResponse(

                syllabus.getId(),

                syllabus.getOrganization() != null
                        ? syllabus.getOrganization().getId()
                        : null,

                organizationName,

                syllabus.getCurriculum() != null
                        ? syllabus.getCurriculum().getId()
                        : null,

                curriculumName,

                curriculumCode,

                syllabus.getClassSubject() != null
                        ? syllabus.getClassSubject().getId()
                        : null,

                subjectId,

                subjectName,

                subjectCode,

                syllabus.getTitle(),

                syllabus.getChapterName(),

                syllabus.getTopicName(),

                syllabus.getDescription(),

                syllabus.getSequenceNo(),

                syllabus.getEstimatedHours(),

                syllabus.getActive(),

                syllabus.getCreatedAt(),

                syllabus.getCreatedBy(),

                syllabus.getUpdatedAt(),

                syllabus.getUpdatedBy()
        );
    }
}