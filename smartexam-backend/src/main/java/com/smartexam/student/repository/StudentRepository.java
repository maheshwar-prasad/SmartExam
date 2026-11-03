package com.smartexam.student.repository;

import com.smartexam.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {

    /**
     * Find Student by Student Code
     */
    @Query("""
            SELECT s
            FROM Student s
            JOIN FETCH s.organization
            JOIN FETCH s.user
            JOIN FETCH s.classRoom
            JOIN FETCH s.section
            WHERE s.studentCode = :studentCode
            """)
    Optional<Student> findByStudentCode(
            @Param("studentCode") String studentCode
    );

    /**
     * Find Student by Admission Number
     */
    Optional<Student> findByAdmissionNo(String admissionNo);

    /**
     * Duplicate Validation
     */
    boolean existsByStudentCodeIgnoreCase(String studentCode);

    boolean existsByStudentCodeIgnoreCaseAndIdNot(
            String studentCode,
            Long id
    );

    boolean existsByAdmissionNoIgnoreCase(String admissionNo);

    boolean existsByAdmissionNoIgnoreCaseAndIdNot(
            String admissionNo,
            Long id
    );

    /**
     * Active Students
     */
    List<Student> findByActiveTrue();

    /**
     * Organization Students
     */
    Page<Student> findByOrganizationId(
            Long organizationId,
            Pageable pageable
    );

    /**
     * Class Students
     */
    Page<Student> findByClassRoomId(
            Long classRoomId,
            Pageable pageable
    );

    /**
     * Section Students
     */
    Page<Student> findBySectionId(
            Long sectionId,
            Pageable pageable
    );

    /**
     * Search Student
     */
    @Query("""
            SELECT s
            FROM Student s
            WHERE

            LOWER(s.studentCode)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            OR

            LOWER(s.admissionNo)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            OR

            LOWER(s.rollNo)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            OR

            LOWER(s.firstName)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            OR

            LOWER(s.lastName)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            OR

            LOWER(s.mobile)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            OR

            LOWER(s.email)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            """)
    Page<Student> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**
     * Dashboard
     */
    long countByActiveTrue();

    long countByOrganizationId(Long organizationId);

    long countByClassRoomId(Long classRoomId);

    long countBySectionId(Long sectionId);

    @Query("""
        SELECT s.classRoom.id, s.classRoom.name, COUNT(s)
        FROM Student s
        WHERE s.active = true
        GROUP BY s.classRoom.id, s.classRoom.name
        ORDER BY s.classRoom.name
        """)
    List<Object[]> countActiveStudentsByClass();

    @Query("""
        SELECT s.section.id, s.section.name, COUNT(s)
        FROM Student s
        WHERE s.active = true
        GROUP BY s.section.id, s.section.name
        ORDER BY s.section.name
        """)
    List<Object[]> countActiveStudentsBySection();

    @Query("""
        SELECT COUNT(s)
        FROM Student s
        WHERE s.active = true
          AND (:organizationId IS NULL
               OR s.organization.id = :organizationId)
          AND (:classRoomId IS NULL
               OR s.classRoom.id = :classRoomId)
          AND (:sectionId IS NULL
               OR s.section.id = :sectionId)
        """)
    long countActiveStudentsByFilter(
            @Param("organizationId") Long organizationId,
            @Param("classRoomId") Long classRoomId,
            @Param("sectionId") Long sectionId
    );
    @Query("""
    SELECT COALESCE(MAX(CAST(s.rollNo AS integer)), 0)
    FROM Student s
    WHERE s.classRoom.id = :classId
      AND s.section.id = :sectionId
""")
    Integer findMaxRollNo(
            @Param("classId") Long classId,
            @Param("sectionId") Long sectionId
    );

    Optional<Student> findByIdAndActiveTrue(Long id);
}