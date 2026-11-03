package com.smartexam.studentenrollment.specification;

import com.smartexam.studentenrollment.entity.StudentEnrollment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class StudentEnrollmentSpecification {

    private StudentEnrollmentSpecification() {
    }

    public static Specification<StudentEnrollment> hasKeyword(
            String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("student").get("firstName")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("student").get("lastName")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("student").get("studentCode")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("student").get("admissionNo")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("rollNo")),
                            pattern)

            );

        };

    }

    public static Specification<StudentEnrollment> hasStudent(
            Long studentId) {

        return (root, query, cb) -> {

            if (studentId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("student").get("id"),
                    studentId);

        };

    }

    public static Specification<StudentEnrollment> hasAcademicSession(
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

    public static Specification<StudentEnrollment> hasClassRoom(
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

    public static Specification<StudentEnrollment> hasSection(
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

    public static Specification<StudentEnrollment> hasEnrollmentStatus(
            com.smartexam.common.enums.EnrollmentStatus status) {

        return (root, query, cb) -> {

            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("enrollmentStatus"),
                    status);

        };

    }

    public static Specification<StudentEnrollment> isActive(
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