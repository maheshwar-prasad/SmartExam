package com.smartexam.academic.specification;

import com.smartexam.academic.entity.AcademicSession;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class AcademicSessionSpecification {

    private AcademicSessionSpecification() {
    }

    public static Specification<AcademicSession> hasKeyword(String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(

                    cb.like(cb.lower(root.get("name")), pattern),

                    cb.like(cb.lower(root.get("code")), pattern),

                    cb.like(cb.lower(root.get("description")), pattern)

            );

        };

    }

    public static Specification<AcademicSession> hasOrganization(Long organizationId) {

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

    public static Specification<AcademicSession> isCurrent(Boolean currentSession) {

        return (root, query, cb) -> {

            if (currentSession == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("currentSession"),
                    currentSession
            );

        };

    }

    public static Specification<AcademicSession> isActive(Boolean active) {

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