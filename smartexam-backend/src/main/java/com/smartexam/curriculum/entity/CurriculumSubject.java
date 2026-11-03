package com.smartexam.curriculum.entity;

import com.smartexam.classsubject.entity.ClassSubject;
import com.smartexam.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "curriculum_subjects",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_curriculum_subject",
                        columnNames = {
                                "curriculum_id",
                                "class_subject_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumSubject extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "curriculum_id",
            nullable = false
    )
    private Curriculum curriculum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "class_subject_id",
            nullable = false
    )
    private ClassSubject classSubject;

    @Column(
            name = "sequence_no",
            nullable = false
    )
    private Integer sequenceNo;

    @Column(
            name = "theory_marks",
            precision = 8,
            scale = 2
    )
    private BigDecimal theoryMarks;

    @Column(
            name = "practical_marks",
            precision = 8,
            scale = 2
    )
    private BigDecimal practicalMarks;

    @Column(
            name = "passing_marks",
            precision = 8,
            scale = 2
    )
    private BigDecimal passingMarks;
}