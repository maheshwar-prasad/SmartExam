package com.smartexam.studentprofile.service;

import com.smartexam.studentprofile.dto.StudentProfileResponse;

public interface StudentProfileService {

    StudentProfileResponse getStudentProfile(Long studentId);
}