package com.smartexam.teacher.specification;

import com.smartexam.teacher.entity.Teacher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class TeacherSpecification {

    private TeacherSpecification() {
    }

    public static Specification<Teacher> hasKeyword(String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("employeeCode")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("firstName")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("lastName")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("email")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("mobile")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("qualification")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("specialization")),
                            pattern)

            );

        };

    }

    public static Specification<Teacher> hasOrganization(
            Long organizationId) {

        return (root, query, cb) -> {

            if (organizationId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("organization").get("id"),
                    organizationId);

        };

    }

    public static Specification<Teacher> isActive(
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