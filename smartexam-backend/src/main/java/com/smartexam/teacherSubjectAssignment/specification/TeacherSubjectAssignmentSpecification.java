package com.smartexam.teacherSubjectAssignment.specification;

import com.smartexam.teacherSubjectAssignment.entity.TeacherSubjectAssignment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class TeacherSubjectAssignmentSpecification {

    private TeacherSubjectAssignmentSpecification() {
    }

    public static Specification<TeacherSubjectAssignment> hasKeyword(
            String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("teacher").get("firstName")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("teacher").get("lastName")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("teacher").get("employeeCode")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("subject").get("name")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("subject").get("code")),
                            pattern)

            );

        };

    }

    public static Specification<TeacherSubjectAssignment> hasTeacher(
            Long teacherId) {

        return (root, query, cb) -> {

            if (teacherId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("teacher").get("id"),
                    teacherId);

        };

    }

    public static Specification<TeacherSubjectAssignment> hasSubject(
            Long subjectId) {

        return (root, query, cb) -> {

            if (subjectId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("subject").get("id"),
                    subjectId);

        };

    }

    public static Specification<TeacherSubjectAssignment> hasClassRoom(
            Long classRoomId) {

        return (root, query, cb) -> {

            if (classRoomId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("classRoom").get("id"),
                    classRoomId);

        };

    }

    public static Specification<TeacherSubjectAssignment> hasSection(
            Long sectionId) {

        return (root, query, cb) -> {

            if (sectionId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("section").get("id"),
                    sectionId);

        };

    }

    public static Specification<TeacherSubjectAssignment> hasAcademicSession(
            Long academicSessionId) {

        return (root, query, cb) -> {

            if (academicSessionId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("academicSession").get("id"),
                    academicSessionId);

        };

    }

    public static Specification<TeacherSubjectAssignment> isClassTeacher(
            Boolean classTeacher) {

        return (root, query, cb) -> {

            if (classTeacher == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("classTeacher"),
                    classTeacher);

        };

    }

    public static Specification<TeacherSubjectAssignment> isActive(
            Boolean active) {

        return (root, query, cb) -> {

            if (active == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("active"),
                    active);

        };

    }

}