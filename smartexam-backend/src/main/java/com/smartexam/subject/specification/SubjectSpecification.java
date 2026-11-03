package com.smartexam.subject.specification;

import com.smartexam.subject.entity.Subject;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class SubjectSpecification {

    private SubjectSpecification() {
    }

    public static Specification<Subject> hasKeyword(String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("name")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("code")),
                            pattern),

                    cb.like(
                            cb.lower(root.get("description")),
                            pattern)

            );

        };

    }

    public static Specification<Subject> hasOrganization(
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

    public static Specification<Subject> hasAcademicSession(
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

    public static Specification<Subject> isActive(
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