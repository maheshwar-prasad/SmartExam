package com.smartexam.studenttransfer.service;

import com.smartexam.studenttransfer.dto.EligibleTransferStudentResponse;
import com.smartexam.studenttransfer.dto.StudentTransferRequest;
import com.smartexam.studenttransfer.dto.StudentTransferResponse;

import java.util.List;

public interface StudentTransferService {

    List<EligibleTransferStudentResponse> getEligibleStudents(
            Long academicSessionId,
            Long classRoomId,
            Long sectionId
    );

    StudentTransferResponse transferStudent(
            StudentTransferRequest request
    );

    StudentTransferResponse getById(
            Long id
    );

    List<StudentTransferResponse> getByStudentId(
            Long studentId
    );
}