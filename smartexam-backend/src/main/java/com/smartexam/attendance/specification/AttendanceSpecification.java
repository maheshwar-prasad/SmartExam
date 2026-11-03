package com.smartexam.attendance.specification;

import com.smartexam.attendance.entity.Attendance;
import com.smartexam.common.enums.AttendanceStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public final class AttendanceSpecification {

    private AttendanceSpecification() {
    }

    public static Specification<Attendance> hasKeyword(String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("studentEnrollment")
                                    .get("student")
                                    .get("firstName")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("studentEnrollment")
                                    .get("student")
                                    .get("lastName")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("studentEnrollment")
                                    .get("student")
                                    .get("studentCode")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("studentEnrollment")
                                    .get("student")
                                    .get("admissionNo")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("studentEnrollment")
                                    .get("rollNo")),
                            pattern)

            );
        };
    }

    public static Specification<Attendance> hasStudentEnrollment(Long studentEnrollmentId) {

        return (root, query, cb) -> {

            if (studentEnrollmentId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("studentEnrollment").get("id"),
                    studentEnrollmentId);
        };
    }

    public static Specification<Attendance> hasTeacherSubjectAssignment(Long teacherSubjectAssignmentId) {

        return (root, query, cb) -> {

            if (teacherSubjectAssignmentId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("teacherSubjectAssignment").get("id"),
                    teacherSubjectAssignmentId);
        };
    }

    public static Specification<Attendance> hasStudent(Long studentId) {

        return (root, query, cb) -> {

            if (studentId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("studentEnrollment")
                            .get("student")
                            .get("id"),
                    studentId);
        };
    }

    public static Specification<Attendance> hasTeacher(Long teacherId) {

        return (root, query, cb) -> {

            if (teacherId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("teacherSubjectAssignment")
                            .get("teacher")
                            .get("id"),
                    teacherId);
        };
    }

    public static Specification<Attendance> hasSubject(Long subjectId) {

        return (root, query, cb) -> {

            if (subjectId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("teacherSubjectAssignment")
                            .get("subject")
                            .get("id"),
                    subjectId);
        };
    }

    public static Specification<Attendance> hasAcademicSession(Long academicSessionId) {

        return (root, query, cb) -> {

            if (academicSessionId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("studentEnrollment")
                            .get("academicSession")
                            .get("id"),
                    academicSessionId);
        };
    }

    public static Specification<Attendance> hasClassRoom(Long classRoomId) {

        return (root, query, cb) -> {

            if (classRoomId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("studentEnrollment")
                            .get("classRoom")
                            .get("id"),
                    classRoomId);
        };
    }

    public static Specification<Attendance> hasSection(Long sectionId) {

        return (root, query, cb) -> {

            if (sectionId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("studentEnrollment")
                            .get("section")
                            .get("id"),
                    sectionId);
        };
    }

    public static Specification<Attendance> hasStatus(AttendanceStatus status) {

        return (root, query, cb) -> {

            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Attendance> hasAttendanceDate(LocalDate attendanceDate) {

        return (root, query, cb) -> {

            if (attendanceDate == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("attendanceDate"), attendanceDate);
        };
    }

    public static Specification<Attendance> betweenDates(
            LocalDate fromDate,
            LocalDate toDate) {

        return (root, query, cb) -> {

            if (fromDate == null || toDate == null) {
                return cb.conjunction();
            }

            return cb.between(
                    root.get("attendanceDate"),
                    fromDate,
                    toDate);
        };
    }

    public static Specification<Attendance> isActive(Boolean active) {

        return (root, query, cb) -> {

            if (active == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("active"), active);
        };
    }
}