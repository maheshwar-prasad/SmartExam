package com.smartexam.curriculum.repository;

import com.smartexam.curriculum.entity.CurriculumSubject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumSubjectRepository
        extends JpaRepository<CurriculumSubject, Long> {

    List<CurriculumSubject>
    findByCurriculumId(
            Long curriculumId
    );


    List<CurriculumSubject>
    findByCurriculumIdAndActiveTrue(
            Long curriculumId
    );


    boolean existsByCurriculumIdAndClassSubjectId(
            Long curriculumId,
            Long classSubjectId
    );


    boolean existsByCurriculumIdAndClassSubjectIdAndIdNot(
            Long curriculumId,
            Long classSubjectId,
            Long id
    );
}