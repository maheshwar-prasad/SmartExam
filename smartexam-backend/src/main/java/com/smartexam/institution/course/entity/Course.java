package com.smartexam.institution.course.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.institution.course.enums.CourseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course extends BaseEntity {

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "campus_id", nullable = false)
    private Long campusId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "program_id", nullable = false)
    private Long programId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "semester")
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type", nullable = false, length = 30)
    private CourseType courseType;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}