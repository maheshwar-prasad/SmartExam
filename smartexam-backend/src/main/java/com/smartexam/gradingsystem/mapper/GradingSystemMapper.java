package com.smartexam.gradingsystem.mapper;

import com.smartexam.gradingsystem.dto.GradingGradeResponse;
import com.smartexam.gradingsystem.dto.GradingSystemResponse;
import com.smartexam.gradingsystem.entity.GradingSystem;
import com.smartexam.gradingsystem.entity.GradingSystemGrade;
import org.springframework.stereotype.Component;

@Component
public class GradingSystemMapper {

    public GradingSystemResponse toResponse(GradingSystem entity) {
        return new GradingSystemResponse(
                entity.getId(),
                entity.getOrganization().getId(),
                entity.getName(),
                entity.getCode(),
                entity.getDescription(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getGrades().stream()
                        .map(this::toGradeResponse)
                        .toList()
        );
    }

    private GradingGradeResponse toGradeResponse(GradingSystemGrade grade) {
        return new GradingGradeResponse(
                grade.getId(),
                grade.getGrade(),
                grade.getMinimumPercentage(),
                grade.getMaximumPercentage(),
                grade.getGradePoint(),
                grade.getRemarks(),
                grade.getPassing(),
                grade.getDisplayOrder()
        );
    }
}