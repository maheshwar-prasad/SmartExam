package com.smartexam.studentenrollment.repository;

import com.smartexam.studentenrollment.entity.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentEnrollmentRepository extends
        JpaRepository<StudentEnrollment, Long>,
        JpaSpecificationExecutor<StudentEnrollment> {

    /**
     * Student already enrolled in Academic Session
     */
    boolean existsByStudentIdAndAcademicSessionId(
            Long studentId,
            Long academicSessionId);

    /**
     * Student already enrolled in Academic Session (Update)
     */
    boolean existsByStudentIdAndAcademicSessionIdAndIdNot(
            Long studentId,
            Long academicSessionId,
            Long id);

    /**
     * Roll Number duplicate
     */
    boolean existsByRollNoAndClassRoomIdAndSectionIdAndAcademicSessionId(
            String rollNo,
            Long classRoomId,
            Long sectionId,
            Long academicSessionId);

    /**
     * Roll Number duplicate (Update)
     */
    boolean existsByRollNoAndClassRoomIdAndSectionIdAndAcademicSessionIdAndIdNot(
            String rollNo,
            Long classRoomId,
            Long sectionId,
            Long academicSessionId,
            Long id);

    /**
     * Active Enrollment
     */
    Optional<StudentEnrollment> findByIdAndActiveTrue(
            Long id);

    /**
     * Student Enrollments
     */
    List<StudentEnrollment> findByStudentIdAndActiveTrue(
            Long studentId);

    /**
     * Class + Section + Session
     */
    List<StudentEnrollment> findByClassRoomIdAndSectionIdAndAcademicSessionIdAndActiveTrue(
            Long classRoomId,
            Long sectionId,
            Long academicSessionId);

    /**
     * Academic Session
     */
    List<StudentEnrollment> findByAcademicSessionIdAndActiveTrue(
            Long academicSessionId);


    List<StudentEnrollment> findByStudentId(Long studentId);

    @Query("""
    SELECT e
    FROM StudentEnrollment e
    JOIN FETCH e.student s
    JOIN FETCH e.academicSession session
    JOIN FETCH e.classRoom classroom
    JOIN FETCH e.section section
    WHERE e.academicSession.id = :academicSessionId
      AND e.classRoom.id = :classRoomId
      AND (:sectionId IS NULL OR e.section.id = :sectionId)
      AND e.active = true
      AND e.enrollmentStatus IN (
          com.smartexam.common.enums.EnrollmentStatus.ACTIVE,
          com.smartexam.common.enums.EnrollmentStatus.PASSED
      )
    ORDER BY s.firstName ASC, s.lastName ASC
    """)
    List<StudentEnrollment> findEligibleForPromotion(
            @Param("academicSessionId") Long academicSessionId,
            @Param("classRoomId") Long classRoomId,
            @Param("sectionId") Long sectionId
    );

    Optional<StudentEnrollment> findByStudentIdAndAcademicSessionId(
            Long studentId,
            Long academicSessionId
    );

    @Query("""
    SELECT e
    FROM StudentEnrollment e
    JOIN FETCH e.student s
    JOIN FETCH e.academicSession session
    JOIN FETCH e.classRoom classroom
    JOIN FETCH e.section section
    WHERE e.academicSession.id = :academicSessionId
      AND e.classRoom.id = :classRoomId
      AND (:sectionId IS NULL OR e.section.id = :sectionId)
      AND e.active = true
      AND e.enrollmentStatus NOT IN (
          com.smartexam.common.enums.EnrollmentStatus.TRANSFERRED,
          com.smartexam.common.enums.EnrollmentStatus.DROPPED
      )
    ORDER BY s.firstName ASC, s.lastName ASC
    """)
    List<StudentEnrollment> findEligibleForTransfer(
            @Param("academicSessionId") Long academicSessionId,
            @Param("classRoomId") Long classRoomId,
            @Param("sectionId") Long sectionId
    );

    Optional<StudentEnrollment> findFirstByStudentIdAndActiveTrueOrderByIdDesc(
            Long studentId
    );

}