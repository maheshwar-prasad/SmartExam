package com.smartexam.teacherSubjectAssignment.repository;

import com.smartexam.teacherSubjectAssignment.entity.TeacherSubjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TeacherSubjectAssignmentRepository extends
        JpaRepository<TeacherSubjectAssignment, Long>,
        JpaSpecificationExecutor<TeacherSubjectAssignment> {

    boolean existsByTeacherIdAndSubjectIdAndClassRoomIdAndSectionIdAndAcademicSessionId(
            Long teacherId,
            Long subjectId,
            Long classRoomId,
            Long sectionId,
            Long academicSessionId);

    boolean existsByTeacherIdAndSubjectIdAndClassRoomIdAndSectionIdAndAcademicSessionIdAndIdNot(
            Long teacherId,
            Long subjectId,
            Long classRoomId,
            Long sectionId,
            Long academicSessionId,
            Long id);

    boolean existsByClassRoomIdAndSectionIdAndAcademicSessionIdAndClassTeacherTrue(
            Long classRoomId,
            Long sectionId,
            Long academicSessionId);

    boolean existsByClassRoomIdAndSectionIdAndAcademicSessionIdAndClassTeacherTrueAndIdNot(
            Long classRoomId,
            Long sectionId,
            Long academicSessionId,
            Long id);

    Optional<TeacherSubjectAssignment> findByIdAndActiveTrue(
            Long id);

}