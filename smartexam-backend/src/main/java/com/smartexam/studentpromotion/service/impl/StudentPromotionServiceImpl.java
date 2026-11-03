package com.smartexam.studentpromotion.service.impl;

import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.common.enums.EnrollmentStatus;
import com.smartexam.section.repository.SectionRepository;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import com.smartexam.studentenrollment.repository.StudentEnrollmentRepository;
import com.smartexam.studentpromotion.dto.EligibleStudentResponse;
import com.smartexam.studentpromotion.dto.StudentPromotionRequest;
import com.smartexam.studentpromotion.dto.StudentPromotionResponse;
import com.smartexam.studentpromotion.service.StudentPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentPromotionServiceImpl
        implements StudentPromotionService {

    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;


    @Override
    @Transactional(readOnly = true)
    public List<EligibleStudentResponse> getEligibleStudents(
            Long academicSessionId,
            Long classRoomId,
            Long sectionId) {

        List<StudentEnrollment> enrollments =
                studentEnrollmentRepository.findEligibleForPromotion(
                        academicSessionId,
                        classRoomId,
                        sectionId
                );

        return enrollments.stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public StudentPromotionResponse promoteStudents(
            StudentPromotionRequest request) {

        /*
         * Resolve target entities
         */
        var targetAcademicSession =
                academicSessionRepository.findById(
                        request.getTargetAcademicSessionId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Target Academic Session not found: "
                                        + request.getTargetAcademicSessionId()
                        )
                );

        var targetClassRoom =
                classRoomRepository.findById(
                        request.getTargetClassRoomId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Target Class Room not found: "
                                        + request.getTargetClassRoomId()
                        )
                );

        var targetSection =
                sectionRepository.findById(
                        request.getTargetSectionId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Target Section not found: "
                                        + request.getTargetSectionId()
                        )
                );


        /*
         * Validate target section belongs to target classroom
         */
        if (!targetSection.getClassRoom()
                .getId()
                .equals(targetClassRoom.getId())) {

            throw new IllegalArgumentException(
                    "Target section does not belong to target class room"
            );
        }


        List<StudentPromotionResponse.PromotionResult> results =
                new ArrayList<>();

        for (Long studentId : request.getStudentIds()) {

            StudentEnrollment oldEnrollment =
                    studentEnrollmentRepository
                            .findByStudentIdAndAcademicSessionId(
                                    studentId,
                                    request.getSourceAcademicSessionId()
                            )
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Source enrollment not found for student: "
                                                    + studentId
                                    )
                            );


            /*
             * Validate source classroom
             */
            if (!oldEnrollment.getClassRoom()
                    .getId()
                    .equals(request.getSourceClassRoomId())) {

                throw new IllegalArgumentException(
                        "Student does not belong to the selected source class: "
                                + studentId
                );
            }


            /*
             * Validate source section
             */
            if (request.getSourceSectionId() != null
                    && !oldEnrollment.getSection()
                    .getId()
                    .equals(request.getSourceSectionId())) {

                throw new IllegalArgumentException(
                        "Student does not belong to the selected source section: "
                                + studentId
                );
            }


            /*
             * Validate active enrollment
             */
            if (!Boolean.TRUE.equals(
                    oldEnrollment.getActive())) {

                throw new IllegalArgumentException(
                        "Student enrollment is inactive: "
                                + studentId
                );
            }


            /*
             * Validate enrollment status
             */
            if (oldEnrollment.getEnrollmentStatus() != EnrollmentStatus.ACTIVE
                    && oldEnrollment.getEnrollmentStatus() != EnrollmentStatus.PASSED) {

                throw new IllegalArgumentException(
                        "Student is not eligible for promotion. "
                                + "Student: "
                                + studentId
                                + ", Status: "
                                + oldEnrollment.getEnrollmentStatus()
                );
            }


            /*
             * Prevent duplicate target enrollment
             */
            boolean targetExists =
                    studentEnrollmentRepository
                            .existsByStudentIdAndAcademicSessionId(
                                    studentId,
                                    request.getTargetAcademicSessionId()
                            );

            if (targetExists) {

                throw new IllegalArgumentException(
                        "Student already has an enrollment in target "
                                + "academic session: "
                                + studentId
                );
            }


            /*
             * Mark old enrollment as PROMOTED
             */
            oldEnrollment.setEnrollmentStatus(
                    EnrollmentStatus.PROMOTED
            );

            studentEnrollmentRepository.save(oldEnrollment);


            /*
             * Create new enrollment
             */
            StudentEnrollment newEnrollment =
                    StudentEnrollment.builder()
                            .student(oldEnrollment.getStudent())
                            .academicSession(targetAcademicSession)
                            .classRoom(targetClassRoom)
                            .section(targetSection)
                            .rollNo(oldEnrollment.getRollNo())
                            .admissionDate(oldEnrollment.getAdmissionDate())
                            .enrollmentStatus(EnrollmentStatus.ACTIVE)
                            .active(true)
                            .build();


            newEnrollment =
                    studentEnrollmentRepository.save(newEnrollment);


            /*
             * Add successful result
             */
            results.add(
                    StudentPromotionResponse.PromotionResult.builder()
                            .studentId(studentId)
                            .studentName(buildStudentName(oldEnrollment))
                            .oldEnrollmentId(oldEnrollment.getId())
                            .newEnrollmentId(newEnrollment.getId())
                            .promoted(true)
                            .message("Student promoted successfully")
                            .build()
            );
        }


        return StudentPromotionResponse.builder()
                .totalStudents(request.getStudentIds().size())
                .promotedStudents(results.size())
                .failedStudents(0)
                .results(results)
                .build();
    }


    private EligibleStudentResponse toResponse(
            StudentEnrollment enrollment) {

        return EligibleStudentResponse.builder()
                .studentId(enrollment.getStudent().getId())
                .enrollmentId(enrollment.getId())
                .studentCode(enrollment.getStudent().getStudentCode())
                .admissionNo(enrollment.getStudent().getAdmissionNo())
                .studentName(buildStudentName(enrollment))
                .rollNo(enrollment.getRollNo())
                .academicSessionId(
                        enrollment.getAcademicSession().getId()
                )
                .academicSessionName(
                        enrollment.getAcademicSession().getName()
                )
                .classRoomId(
                        enrollment.getClassRoom().getId()
                )
                .classRoomName(
                        enrollment.getClassRoom().getName()
                )
                .sectionId(
                        enrollment.getSection().getId()
                )
                .sectionName(
                        enrollment.getSection().getName()
                )
                .admissionDate(enrollment.getAdmissionDate())
                .enrollmentStatus(enrollment.getEnrollmentStatus())
                .active(enrollment.getActive())
                .build();
    }


    private String buildStudentName(
            StudentEnrollment enrollment) {

        String firstName =
                enrollment.getStudent().getFirstName();

        String lastName =
                enrollment.getStudent().getLastName();

        return ((firstName == null ? "" : firstName)
                + " "
                + (lastName == null ? "" : lastName))
                .trim();
    }
}