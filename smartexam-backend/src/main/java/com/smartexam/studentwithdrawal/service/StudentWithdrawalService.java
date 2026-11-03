package com.smartexam.studentwithdrawal.service;

import com.smartexam.studentwithdrawal.dto.StudentWithdrawalRequest;
import com.smartexam.studentwithdrawal.dto.StudentWithdrawalResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentWithdrawalService {

    StudentWithdrawalResponse create(
            StudentWithdrawalRequest request
    );

    StudentWithdrawalResponse update(
            Long id,
            StudentWithdrawalRequest request
    );

    StudentWithdrawalResponse getById(Long id);

    Page<StudentWithdrawalResponse> getAll(
            Pageable pageable
    );

    void activate(Long id);

    void deactivate(Long id);
}