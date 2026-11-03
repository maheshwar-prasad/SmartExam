package com.smartexam.student.specification;

import com.smartexam.student.entity.Student;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class StudentSpecification {

    private StudentSpecification() {
    }

    public static Specification<Student> hasKeyword(String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(

                    cb.like(cb.lower(root.get("studentCode")), pattern),

                    cb.like(cb.lower(root.get("admissionNo")), pattern),

                    cb.like(cb.lower(root.get("rollNo")), pattern),

                    cb.like(cb.lower(root.get("firstName")), pattern),

                    cb.like(cb.lower(root.get("lastName")), pattern),

                    cb.like(cb.lower(root.get("mobile")), pattern),

                    cb.like(cb.lower(root.get("email")), pattern)

            );

        };

    }

    public static Specification<Student> hasOrganization(Long organizationId) {

        return (root, query, cb) -> {

            if (organizationId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("organization").get("id"),
                    organizationId
            );

        };

    }

    public static Specification<Student> hasClass(Long classId) {

        return (root, query, cb) -> {

            if (classId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("classRoom").get("id"),
                    classId
            );

        };

    }

    public static Specification<Student> hasSection(Long sectionId) {

        return (root, query, cb) -> {

            if (sectionId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("section").get("id"),
                    sectionId
            );

        };

    }

    public static Specification<Student> isActive(Boolean active) {

        return (root, query, cb) -> {

            if (active == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("active"),
                    active
            );

        };

    }

}