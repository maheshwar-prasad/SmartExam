package com.smartexam.gradingsystem.entity;

import com.smartexam.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "grading_system_grades",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_grading_system_grade",
                columnNames = {"grading_system_id", "grade"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class GradingSystemGrade extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grading_system_id", nullable = false)
    private GradingSystem gradingSystem;

    @Column(nullable = false, length = 20)
    private String grade;

    @Column(name = "minimum_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal minimumPercentage;

    @Column(name = "maximum_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal maximumPercentage;

    @Column(name = "grade_point", precision = 4, scale = 2)
    private BigDecimal gradePoint;

    @Column(length = 300)
    private String remarks;

    @Column(nullable = false)
    private Boolean passing = true;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 1;
}