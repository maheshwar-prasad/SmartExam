package com.smartexam.permission.specification;

import com.smartexam.permission.entity.Permission;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class PermissionSpecification {

    private PermissionSpecification() {
    }

    /**
     * Search by keyword (name, code, description)
     */
    public static Specification<Permission> hasKeyword(String keyword) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("code")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    /**
     * Filter by Module
     */
    public static Specification<Permission> hasModule(String module) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(module)) {
                return cb.conjunction();
            }

            return cb.equal(
                    cb.lower(root.get("module")),
                    module.toLowerCase()
            );
        };
    }

    /**
     * Filter by Operation
     */
    public static Specification<Permission> hasOperation(String operation) {

        return (root, query, cb) -> {

            if (!StringUtils.hasText(operation)) {
                return cb.conjunction();
            }

            return cb.equal(
                    cb.lower(root.get("operation")),
                    operation.toLowerCase()
            );
        };
    }

    /**
     * Filter by Active Status
     */
    public static Specification<Permission> isActive(Boolean active) {

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