package com.smartexam.studentprofile.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.common.enums.EnrollmentStatus;
import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.section.entity.Section;
import com.smartexam.student.entity.Student;
import com.smartexam.student.repository.StudentRepository;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import com.smartexam.studentenrollment.repository.StudentEnrollmentRepository;
import com.smartexam.studentprofile.dto.StudentProfileResponse;
import com.smartexam.studentprofile.service.StudentProfileService;
import com.smartexam.studenttransfer.entity.StudentTransfer;
import com.smartexam.studenttransfer.repository.StudentTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final StudentTransferRepository studentTransferRepository;

    @Override
    public StudentProfileResponse getStudentProfile(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : " + studentId
                        )
                );

        /*
         * Current enrollment
         *
         * After promotion/transfer, the latest ACTIVE enrollment
         * represents the student's current academic position.
         */
        StudentProfileResponse.CurrentEnrollment currentEnrollment =
                studentEnrollmentRepository
                        .findByStudentIdAndActiveTrue(studentId)
                        .stream()
                        .filter(enrollment ->
                                enrollment.getEnrollmentStatus() == EnrollmentStatus.ACTIVE)
                        .findFirst()
                        .map(this::mapCurrentEnrollment)
                        .orElse(null);

        /*
         * Complete enrollment history
         */
        List<StudentProfileResponse.EnrollmentHistory> enrollmentHistory =
                studentEnrollmentRepository
                        .findByStudentId(studentId)
                        .stream()
                        .map(this::mapEnrollmentHistory)
                        .toList();

        /*
         * Complete transfer history
         */
        List<StudentProfileResponse.TransferHistory> transferHistory =
                studentTransferRepository
                        .findByStudentIdOrderByTransferDateDesc(studentId)
                        .stream()
                        .map(this::mapTransferHistory)
                        .toList();

        return StudentProfileResponse.builder()
                .student(mapStudent(student))
                .currentEnrollment(currentEnrollment)
                .enrollmentHistory(enrollmentHistory)
                .transferHistory(transferHistory)
                .build();
    }

    // =========================================================
    // Student
    // =========================================================

    private StudentProfileResponse.StudentInfo mapStudent(Student student) {

        return StudentProfileResponse.StudentInfo.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .admissionNo(student.getAdmissionNo())
                .rollNo(student.getRollNo())
                .admissionDate(student.getAdmissionDate())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .gender(String.valueOf(student.getGender()))
                .bloodGroup(student.getBloodGroup())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .guardianName(student.getGuardianName())
                .mobile(student.getMobile())
                .email(student.getEmail())
                .address(student.getAddress())
                .photoUrl(student.getPhotoUrl())
                .organizationId(
                        student.getOrganization() != null
                                ? student.getOrganization().getId()
                                : null
                )
                .organizationName(
                        student.getOrganization() != null
                                ? student.getOrganization().getName()
                                : null
                )
                .active(student.getActive())
                .build();
    }

    // =========================================================
    // Current Enrollment
    // =========================================================

    private StudentProfileResponse.CurrentEnrollment mapCurrentEnrollment(
            StudentEnrollment enrollment) {

        AcademicSession session = enrollment.getAcademicSession();

        return StudentProfileResponse.CurrentEnrollment.builder()
                .enrollmentId(enrollment.getId())
                .academicSessionId(
                        session != null ? session.getId() : null
                )
                .academicSessionName(
                        session != null ? session.getName() : null
                )
                .classRoomId(
                        enrollment.getClassRoom() != null
                                ? enrollment.getClassRoom().getId()
                                : null
                )
                .classRoomName(
                        enrollment.getClassRoom() != null
                                ? enrollment.getClassRoom().getName()
                                : null
                )
                .sectionId(
                        enrollment.getSection() != null
                                ? enrollment.getSection().getId()
                                : null
                )
                .sectionName(
                        enrollment.getSection() != null
                                ? enrollment.getSection().getName()
                                : null
                )
                .rollNo(enrollment.getRollNo())
                .admissionDate(enrollment.getAdmissionDate())
                .enrollmentStatus(enrollment.getEnrollmentStatus())
                .active(enrollment.getActive())
                .build();
    }

    // =========================================================
    // Enrollment History
    // =========================================================

    private StudentProfileResponse.EnrollmentHistory mapEnrollmentHistory(
            StudentEnrollment enrollment) {

        AcademicSession session = enrollment.getAcademicSession();

        return StudentProfileResponse.EnrollmentHistory.builder()
                .enrollmentId(enrollment.getId())
                .academicSessionId(
                        session != null ? session.getId() : null
                )
                .academicSessionName(
                        session != null ? session.getName() : null
                )
                .classRoomId(
                        enrollment.getClassRoom() != null
                                ? enrollment.getClassRoom().getId()
                                : null
                )
                .classRoomName(
                        enrollment.getClassRoom() != null
                                ? enrollment.getClassRoom().getName()
                                : null
                )
                .sectionId(
                        enrollment.getSection() != null
                                ? enrollment.getSection().getId()
                                : null
                )
                .sectionName(
                        enrollment.getSection() != null
                                ? enrollment.getSection().getName()
                                : null
                )
                .rollNo(enrollment.getRollNo())
                .admissionDate(enrollment.getAdmissionDate())
                .enrollmentStatus(enrollment.getEnrollmentStatus())
                .active(enrollment.getActive())
                .build();
    }

    // =========================================================
    // Transfer History
    // =========================================================

    private StudentProfileResponse.TransferHistory mapTransferHistory(
            StudentTransfer transfer) {

        return StudentProfileResponse.TransferHistory.builder()
                .transferId(transfer.getId())

                .sourceAcademicSessionId(
                        transfer.getSourceAcademicSessionId()
                )
                .sourceClassRoomId(
                        transfer.getSourceClassRoomId()
                )
                .sourceSectionId(
                        transfer.getSourceSectionId()
                )

                .targetAcademicSessionId(
                        transfer.getTargetAcademicSessionId()
                )
                .targetClassRoomId(
                        transfer.getTargetClassRoomId()
                )
                .targetSectionId(
                        transfer.getTargetSectionId()
                )

                .transferDate(transfer.getTransferDate())
                .reason(transfer.getReason())
                .remarks(transfer.getRemarks())
                .build();
    }
}