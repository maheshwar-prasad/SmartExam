package com.smartexam.studentidcard.service;

import com.smartexam.studentidcard.dto.StudentIdCardBulkRequest;
import com.smartexam.studentidcard.dto.StudentIdCardBulkResponse;
import com.smartexam.studentidcard.dto.StudentIdCardGenerateRequest;
import com.smartexam.studentidcard.dto.StudentIdCardResponse;
import org.springframework.core.io.Resource;

import java.util.List;

public interface StudentIdCardService {

    StudentIdCardResponse generate(
            Long studentId,
            StudentIdCardGenerateRequest request
    );

    StudentIdCardResponse getById(
            Long id
    );

    List<StudentIdCardResponse> getByStudentId(
            Long studentId
    );

    StudentIdCardBulkResponse generateBulk(
            StudentIdCardBulkRequest request
    );

    Resource download(
            Long id
    );

    void activate(
            Long id
    );

    void deactivate(
            Long id
    );
}