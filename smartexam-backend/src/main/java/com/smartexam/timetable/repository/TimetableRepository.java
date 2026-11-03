package com.smartexam.timetable.repository;

import com.smartexam.common.enums.DayOfWeek;
import com.smartexam.timetable.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimetableRepository extends
        JpaRepository<Timetable, Long>,
        JpaSpecificationExecutor<Timetable> {

    List<Timetable> findByActiveTrue();

    List<Timetable> findByTeacherSubjectAssignmentIdAndActiveTrue(
            Long teacherSubjectAssignmentId);

    /**
     * CREATE:
     * Check teacher time conflict.
     */
    @Query("""
            SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
            FROM Timetable t
            WHERE t.teacherSubjectAssignment.teacher.id = :teacherId
              AND t.dayOfWeek = :dayOfWeek
              AND t.active = true
              AND t.startTime < :endTime
              AND t.endTime > :startTime
            """)
    boolean existsTeacherTimeConflict(
            @Param("teacherId") Long teacherId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * CREATE:
     * Check ClassRoom + Section time conflict.
     */
    @Query("""
            SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
            FROM Timetable t
            WHERE t.teacherSubjectAssignment.classRoom.id = :classRoomId
              AND t.teacherSubjectAssignment.section.id = :sectionId
              AND t.dayOfWeek = :dayOfWeek
              AND t.active = true
              AND t.startTime < :endTime
              AND t.endTime > :startTime
            """)
    boolean existsClassSectionTimeConflict(
            @Param("classRoomId") Long classRoomId,
            @Param("sectionId") Long sectionId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * UPDATE:
     * Check teacher time conflict excluding current timetable.
     */
    @Query("""
            SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
            FROM Timetable t
            WHERE t.teacherSubjectAssignment.teacher.id = :teacherId
              AND t.dayOfWeek = :dayOfWeek
              AND t.active = true
              AND t.id <> :timetableId
              AND t.startTime < :endTime
              AND t.endTime > :startTime
            """)
    boolean existsTeacherTimeConflictExcludingId(
            @Param("timetableId") Long timetableId,
            @Param("teacherId") Long teacherId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * UPDATE:
     * Check ClassRoom + Section time conflict
     * excluding the current timetable.
     */
    @Query("""
            SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
            FROM Timetable t
            WHERE t.teacherSubjectAssignment.classRoom.id = :classRoomId
              AND t.teacherSubjectAssignment.section.id = :sectionId
              AND t.dayOfWeek = :dayOfWeek
              AND t.active = true
              AND t.id <> :timetableId
              AND t.startTime < :endTime
              AND t.endTime > :startTime
            """)
    boolean existsClassSectionTimeConflictExcludingId(
            @Param("timetableId") Long timetableId,
            @Param("classRoomId") Long classRoomId,
            @Param("sectionId") Long sectionId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}