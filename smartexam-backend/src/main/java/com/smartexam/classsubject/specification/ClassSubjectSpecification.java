package com.smartexam.classsubject.specification;

import com.smartexam.classsubject.entity.ClassSubject;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class ClassSubjectSpecification {

    private ClassSubjectSpecification() {
    }

    public static Specification<ClassSubject> keyword(
            String keyword
    ) {

        return (root, query, criteriaBuilder) -> {

            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String value =
                    "%" + keyword.trim().toLowerCase() + "%";

            Join<?, ?> classRoom =
                    root.join("classRoom", JoinType.LEFT);

            Join<?, ?> subject =
                    root.join("subject", JoinType.LEFT);

            Join<?, ?> academicSession =
                    root.join(
                            "academicSession",
                            JoinType.LEFT
                    );

            return criteriaBuilder.or(

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    classRoom.get("name")
                            ),
                            value
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    classRoom.get("code")
                            ),
                            value
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    subject.get("name")
                            ),
                            value
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    subject.get("code")
                            ),
                            value
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    academicSession.get("name")
                            ),
                            value
                    )
            );
        };
    }

    public static Specification<ClassSubject> organizationId(
            Long organizationId
    ) {

        return (root, query, criteriaBuilder) -> {

            if (organizationId == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("organization").get("id"),
                    organizationId
            );
        };
    }

    public static Specification<ClassSubject> academicSessionId(
            Long academicSessionId
    ) {

        return (root, query, criteriaBuilder) -> {

            if (academicSessionId == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("academicSession").get("id"),
                    academicSessionId
            );
        };
    }

    public static Specification<ClassSubject> classRoomId(
            Long classRoomId
    ) {

        return (root, query, criteriaBuilder) -> {

            if (classRoomId == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("classRoom").get("id"),
                    classRoomId
            );
        };
    }

    public static Specification<ClassSubject> subjectId(
            Long subjectId
    ) {

        return (root, query, criteriaBuilder) -> {

            if (subjectId == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("subject").get("id"),
                    subjectId
            );
        };
    }

    public static Specification<ClassSubject> active(
            Boolean active
    ) {

        return (root, query, criteriaBuilder) -> {

            if (active == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("active"),
                    active
            );
        };
    }
}