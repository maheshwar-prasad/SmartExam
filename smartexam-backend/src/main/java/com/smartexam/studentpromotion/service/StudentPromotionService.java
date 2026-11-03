package com.smartexam.studentpromotion.service;

import com.smartexam.studentpromotion.dto.EligibleStudentResponse;
import com.smartexam.studentpromotion.dto.StudentPromotionRequest;
import com.smartexam.studentpromotion.dto.StudentPromotionResponse;

import java.util.List;

public interface StudentPromotionService {

    List<EligibleStudentResponse> getEligibleStudents(
            Long academicSessionId,
            Long classRoomId,
            Long sectionId
    );

    StudentPromotionResponse promoteStudents(
            StudentPromotionRequest request
    );
}