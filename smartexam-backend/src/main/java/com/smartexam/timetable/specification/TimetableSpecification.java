package com.smartexam.timetable.specification;

import com.smartexam.timetable.dto.TimetableSearchRequest;
import com.smartexam.timetable.entity.Timetable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

public final class TimetableSpecification {

    private TimetableSpecification() {
    }

    public static Specification<Timetable> withFilters(
            TimetableSearchRequest request) {

        return (root, query, criteriaBuilder) -> {

            List<jakarta.persistence.criteria.Predicate> predicates =
                    new ArrayList<>();

            var assignment = root.join(
                    "teacherSubjectAssignment",
                    JoinType.LEFT);

            if (request.getTeacherSubjectAssignmentId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                assignment.get("id"),
                                request.getTeacherSubjectAssignmentId()));
            }

            if (request.getTeacherId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                assignment.join("teacher").get("id"),
                                request.getTeacherId()));
            }

            if (request.getSubjectId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                assignment.join("subject").get("id"),
                                request.getSubjectId()));
            }

            if (request.getAcademicSessionId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                assignment.join("academicSession").get("id"),
                                request.getAcademicSessionId()));
            }

            if (request.getClassRoomId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                assignment.join("classRoom").get("id"),
                                request.getClassRoomId()));
            }

            if (request.getSectionId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                assignment.join("section").get("id"),
                                request.getSectionId()));
            }

            if (request.getDayOfWeek() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("dayOfWeek"),
                                request.getDayOfWeek()));
            }

            if (request.getStartTime() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("startTime"),
                                request.getStartTime()));
            }

            if (request.getEndTime() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("endTime"),
                                request.getEndTime()));
            }

            if (request.getActive() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("active"),
                                request.getActive()));
            }

            if (request.getKeyword() != null
                    && !request.getKeyword().isBlank()) {

                String keyword =
                        "%" + request.getKeyword()
                                .trim()
                                .toLowerCase() + "%";

                var teacher = assignment.join(
                        "teacher",
                        JoinType.LEFT);

                var subject = assignment.join(
                        "subject",
                        JoinType.LEFT);

                predicates.add(
                        criteriaBuilder.or(

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                teacher.get("firstName")),
                                        keyword),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                teacher.get("lastName")),
                                        keyword),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                subject.get("name")),
                                        keyword),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("roomNumber")),
                                        keyword)
                        ));
            }

            return criteriaBuilder.and(
                    predicates.toArray(
                            new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}