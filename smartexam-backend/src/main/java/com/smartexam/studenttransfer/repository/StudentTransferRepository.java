package com.smartexam.studenttransfer.repository;

import com.smartexam.studenttransfer.entity.StudentTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentTransferRepository
        extends JpaRepository<StudentTransfer, Long> {

    List<StudentTransfer> findByStudentIdOrderByTransferDateDesc(
            Long studentId
    );

    Page<StudentTransfer> findByStudentId(
            Long studentId,
            Pageable pageable
    );

    List<StudentTransfer> findBySourceEnrollmentIdOrderByTransferDateDesc(
            Long sourceEnrollmentId
    );

    List<StudentTransfer> findByTargetEnrollmentIdOrderByTransferDateDesc(
            Long targetEnrollmentId
    );
}