package com.smartexam.question.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import com.smartexam.question.enums.DifficultyLevel;
import com.smartexam.question.enums.QuestionType;
import com.smartexam.subject.entity.Subject;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "questions",
        indexes = {
                @Index(
                        name = "idx_question_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_question_subject",
                        columnList = "subject_id"
                ),
                @Index(
                        name = "idx_question_type",
                        columnList = "question_type"
                ),
                @Index(
                        name = "idx_question_difficulty",
                        columnList = "difficulty"
                ),
                @Index(
                        name = "idx_question_active",
                        columnList = "active"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Question extends BaseEntity {

    /**
     * Question text.
     */
    @Column(
            name = "question_text",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String questionText;

    /**
     * Question type.
     *
     * Example:
     * MCQ
     * TRUE_FALSE
     * SHORT_ANSWER
     * DESCRIPTIVE
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "question_type",
            nullable = false,
            length = 30
    )
    private QuestionType questionType;

    /**
     * Question difficulty.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "difficulty",
            nullable = false,
            length = 20
    )
    private DifficultyLevel difficulty;

    /**
     * MCQ Option A.
     */
    @Column(
            name = "option_a",
            columnDefinition = "TEXT"
    )
    private String optionA;

    /**
     * MCQ Option B.
     */
    @Column(
            name = "option_b",
            columnDefinition = "TEXT"
    )
    private String optionB;

    /**
     * MCQ Option C.
     */
    @Column(
            name = "option_c",
            columnDefinition = "TEXT"
    )
    private String optionC;

    /**
     * MCQ Option D.
     */
    @Column(
            name = "option_d",
            columnDefinition = "TEXT"
    )
    private String optionD;

    /**
     * Correct answer.
     *
     * For MCQ this should match one of optionA/B/C/D.
     */
    @Column(
            name = "correct_answer",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String correctAnswer;

    /**
     * Explanation of the correct answer.
     */
    @Column(
            name = "explanation",
            columnDefinition = "TEXT"
    )
    private String explanation;

    /**
     * Marks awarded for the question.
     */
    @Column(
            name = "marks",
            nullable = false
    )
    private Integer marks;

    /**
     * Organization ownership.
     *
     * Every question belongs to one organization.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_question_organization"
            )
    )
    private Organization organization;

    /**
     * Subject associated with the question.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "subject_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_question_subject"
            )
    )
    private Subject subject;

    /**
     * Soft-delete / active flag.
     */
    @Builder.Default
    @Column(
            name = "active",
            nullable = false
    )
    private Boolean active = true;

    /**
     * Set safe defaults before inserting a new question.
     */
    @PrePersist
    protected void onCreate() {

        if (active == null) {
            active = true;
        }

        if (marks == null) {
            marks = 1;
        }
    }
}