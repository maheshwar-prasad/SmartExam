package com.smartexam.syllabus.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.classsubject.entity.ClassSubject;
import com.smartexam.curriculum.entity.Curriculum;
import com.smartexam.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "syllabuses",
        indexes = {
                @Index(
                        name = "idx_syllabus_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_syllabus_curriculum",
                        columnList = "curriculum_id"
                ),
                @Index(
                        name = "idx_syllabus_class_subject",
                        columnList = "class_subject_id"
                ),
                @Index(
                        name = "idx_syllabus_active",
                        columnList = "active"
                ),
                @Index(
                        name = "idx_syllabus_sequence",
                        columnList = "sequence_no"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Syllabus extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false
    )
    private Organization organization;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "curriculum_id",
            nullable = false
    )
    private Curriculum curriculum;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "class_subject_id",
            nullable = false
    )
    private ClassSubject classSubject;


    @Column(
            name = "title",
            nullable = false,
            length = 200
    )
    private String title;


    @Column(
            name = "chapter_name",
            nullable = false,
            length = 200
    )
    private String chapterName;


    @Column(
            name = "topic_name",
            length = 300
    )
    private String topicName;


    @Column(
            name = "description",
            length = 1000
    )
    private String description;


    @Column(
            name = "sequence_no",
            nullable = false
    )
    private Integer sequenceNo;


    @Column(
            name = "estimated_hours",
            precision = 5,
            scale = 2
    )
    private BigDecimal estimatedHours;


    @Column(
            name = "active",
            nullable = false
    )
    private Boolean active = true;
}