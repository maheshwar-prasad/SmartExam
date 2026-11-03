package com.smartexam.gradingsystem.repository;

import com.smartexam.gradingsystem.entity.GradingSystemGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradingSystemGradeRepository
        extends JpaRepository<GradingSystemGrade, Long> {

    List<GradingSystemGrade>
    findByGradingSystem_IdOrderByDisplayOrderAsc(Long gradingSystemId);

    void deleteByGradingSystem_Id(Long gradingSystemId);
}