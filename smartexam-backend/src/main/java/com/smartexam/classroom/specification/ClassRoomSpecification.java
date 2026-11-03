package com.smartexam.classroom.specification;

import com.smartexam.classroom.entity.ClassRoom;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class ClassRoomSpecification {

    private ClassRoomSpecification() {
    }

    /**
     * Search by Name, Code or Description
     */
    public static Specification<ClassRoom> hasKeyword(String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.trim().toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("name")),
                            pattern
                    ),

                    cb.like(
                            cb.lower(root.get("code")),
                            pattern
                    ),

                    cb.like(
                            cb.lower(root.get("description")),
                            pattern
                    )

            );

        };

    }

    /**
     * Filter by Organization
     */
    public static Specification<ClassRoom> hasOrganization(
            Long organizationId) {

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

    /**
     * Filter by Academic Session
     */
    public static Specification<ClassRoom> hasAcademicSession(
            Long academicSessionId) {

        return (root, query, cb) -> {

            if (academicSessionId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("academicSession").get("id"),
                    academicSessionId
            );

        };

    }

    /**
     * Filter by Active Status
     */
    public static Specification<ClassRoom> isActive(Boolean active) {

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