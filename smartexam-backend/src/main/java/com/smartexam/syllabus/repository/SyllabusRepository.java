package com.smartexam.syllabus.repository;

import com.smartexam.syllabus.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SyllabusRepository
        extends JpaRepository<Syllabus, Long> {


    List<Syllabus> findByOrganization_Id(
            Long organizationId
    );


    List<Syllabus> findByCurriculum_Id(
            Long curriculumId
    );


    List<Syllabus> findByClassSubject_Id(
            Long classSubjectId
    );


    List<Syllabus> findByCurriculum_IdAndActiveTrue(
            Long curriculumId
    );


    List<Syllabus> findByCurriculum_IdOrderBySequenceNoAsc(
            Long curriculumId
    );


    List<Syllabus>
    findByCurriculum_IdAndClassSubject_IdOrderBySequenceNoAsc(
            Long curriculumId,
            Long classSubjectId
    );


    boolean existsByCurriculum_IdAndClassSubject_IdAndChapterNameAndTopicName(
            Long curriculumId,
            Long classSubjectId,
            String chapterName,
            String topicName
    );
}