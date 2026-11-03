package com.smartexam.studentidcard.repository;

import com.smartexam.studentidcard.entity.StudentIdCard;
import com.smartexam.studentidcard.enums.IdCardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentIdCardRepository
        extends JpaRepository<StudentIdCard, Long> {


    List<StudentIdCard>
    findByStudentIdAndActiveTrue(Long studentId);


    Optional<StudentIdCard>
    findByIdAndActiveTrue(Long id);


    Optional<StudentIdCard>
    findByCardNumber(String cardNumber);


    boolean existsByCardNumber(String cardNumber);


    boolean existsByStudentIdAndAcademicSessionIdAndActiveTrue(
            Long studentId,
            Long academicSessionId
    );


    List<StudentIdCard>
    findByStudentIdAndStatusAndActiveTrue(
            Long studentId,
            IdCardStatus status
    );
}