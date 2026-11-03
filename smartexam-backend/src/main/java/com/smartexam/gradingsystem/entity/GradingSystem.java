package com.smartexam.gradingsystem.entity;

import com.smartexam.organization.entity.Organization;
import com.smartexam.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "grading_systems",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_grading_system_code",
                columnNames = {"organization_id", "code"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class GradingSystem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(
            mappedBy = "gradingSystem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<GradingSystemGrade> grades = new ArrayList<>();

    public void addGrade(GradingSystemGrade grade) {
        grades.add(grade);
        grade.setGradingSystem(this);
    }

    public void removeGrade(GradingSystemGrade grade) {
        grades.remove(grade);
        grade.setGradingSystem(null);
    }
}