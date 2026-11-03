package com.smartexam.studenttransfer.service.impl;

import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.academic.repository.AcademicSessionRepository;
import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.section.entity.Section;
import com.smartexam.section.repository.SectionRepository;
import com.smartexam.studentenrollment.entity.StudentEnrollment;
import com.smartexam.studentenrollment.repository.StudentEnrollmentRepository;
import com.smartexam.studenttransfer.dto.EligibleTransferStudentResponse;
import com.smartexam.studenttransfer.dto.StudentTransferRequest;
import com.smartexam.studenttransfer.dto.StudentTransferResponse;
import com.smartexam.studenttransfer.entity.StudentTransfer;
import com.smartexam.studenttransfer.repository.StudentTransferRepository;
import com.smartexam.studenttransfer.service.StudentTransferService;
import com.smartexam.common.enums.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentTransferServiceImpl
        implements StudentTransferService {

    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final StudentTransferRepository studentTransferRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;


    @Override
    @Transactional(readOnly = true)
    public List<EligibleTransferStudentResponse> getEligibleStudents(
            Long academicSessionId,
            Long classRoomId,
            Long sectionId) {

        List<StudentEnrollment> enrollments =
                studentEnrollmentRepository.findEligibleForTransfer(
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
    public StudentTransferResponse transferStudent(
            StudentTransferRequest request) {

        // ---------------------------------------------------------
        // 1. Validate source enrollment
        // ---------------------------------------------------------

        StudentEnrollment sourceEnrollment =
                studentEnrollmentRepository
                        .findByStudentIdAndAcademicSessionId(
                                request.getStudentId(),
                                request.getSourceAcademicSessionId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No enrollment found for student "
                                                + request.getStudentId()
                                                + " in source academic session."
                                )
                        );


        // ---------------------------------------------------------
        // 2. Validate source enrollment details
        // ---------------------------------------------------------

        if (!sourceEnrollment.getActive()) {
            throw new IllegalArgumentException(
                    "Source enrollment is not active."
            );
        }

        if (sourceEnrollment.getEnrollmentStatus()
                == EnrollmentStatus.TRANSFERRED) {

            throw new IllegalArgumentException(
                    "Student has already been transferred."
            );
        }

        if (!sourceEnrollment.getClassRoom().getId()
                .equals(request.getSourceClassRoomId())) {

            throw new IllegalArgumentException(
                    "Student is not enrolled in the specified source class."
            );
        }

        if (!sourceEnrollment.getSection().getId()
                .equals(request.getSourceSectionId())) {

            throw new IllegalArgumentException(
                    "Student is not enrolled in the specified source section."
            );
        }


        // ---------------------------------------------------------
        // 3. Load target master data
        // ---------------------------------------------------------

        AcademicSession targetSession =
                academicSessionRepository
                        .findById(request.getTargetAcademicSessionId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Target academic session not found."
                                )
                        );

        ClassRoom targetClassRoom =
                classRoomRepository
                        .findById(request.getTargetClassRoomId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Target class not found."
                                )
                        );

        Section targetSection =
                sectionRepository
                        .findById(request.getTargetSectionId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Target section not found."
                                )
                        );


        // ---------------------------------------------------------
        // 4. Validate target class/section relationship
        // ---------------------------------------------------------

        if (!targetSection.getClassRoom().getId()
                .equals(targetClassRoom.getId())) {

            throw new IllegalArgumentException(
                    "Target section does not belong to the target class."
            );
        }


        // ---------------------------------------------------------
        // 5. Validate target session
        // ---------------------------------------------------------

        if (targetClassRoom.getAcademicSession() != null
                && !targetClassRoom.getAcademicSession()
                .getId()
                .equals(targetSession.getId())) {

            throw new IllegalArgumentException(
                    "Target class does not belong to the target academic session."
            );
        }


        // ---------------------------------------------------------
        // 6. Prevent transfer to the same class/section
        // ---------------------------------------------------------

        if (sourceEnrollment.getAcademicSession().getId()
                .equals(targetSession.getId())
                && sourceEnrollment.getClassRoom().getId()
                .equals(targetClassRoom.getId())
                && sourceEnrollment.getSection().getId()
                .equals(targetSection.getId())) {

            throw new IllegalArgumentException(
                    "Source and target class/section cannot be the same."
            );
        }


        // ---------------------------------------------------------
        // 7. Handle SAME academic session transfer
        // ---------------------------------------------------------

        if (sourceEnrollment.getAcademicSession().getId()
                .equals(targetSession.getId())) {

            return transferWithinSameSession(
                    sourceEnrollment,
                    targetSession,
                    targetClassRoom,
                    targetSection,
                    request
            );
        }


        // ---------------------------------------------------------
        // 8. Handle CROSS academic session transfer
        // ---------------------------------------------------------

        return transferToDifferentSession(
                sourceEnrollment,
                targetSession,
                targetClassRoom,
                targetSection,
                request
        );
    }


    private StudentTransferResponse transferWithinSameSession(
            StudentEnrollment sourceEnrollment,
            AcademicSession targetSession,
            ClassRoom targetClassRoom,
            Section targetSection,
            StudentTransferRequest request) {

        // Keep original source information before modifying enrollment.
        Long sourceSessionId =
                sourceEnrollment.getAcademicSession().getId();

        String sourceSessionName =
                sourceEnrollment.getAcademicSession().getName();

        Long sourceClassId =
                sourceEnrollment.getClassRoom().getId();

        String sourceClassName =
                sourceEnrollment.getClassRoom().getName();

        Long sourceSectionId =
                sourceEnrollment.getSection().getId();

        String sourceSectionName =
                sourceEnrollment.getSection().getName();


        // ---------------------------------------------------------
        // Update existing enrollment
        // ---------------------------------------------------------

        sourceEnrollment.setClassRoom(targetClassRoom);
        sourceEnrollment.setSection(targetSection);
        sourceEnrollment.setEnrollmentStatus(
                EnrollmentStatus.ACTIVE
        );
        sourceEnrollment.setActive(true);

        StudentEnrollment updatedEnrollment =
                studentEnrollmentRepository.save(sourceEnrollment);


        // ---------------------------------------------------------
        // Create transfer history
        // ---------------------------------------------------------

        StudentTransfer transfer =
                StudentTransfer.builder()
                        .student(updatedEnrollment.getStudent())
                        .sourceEnrollment(updatedEnrollment)
                        .targetEnrollment(updatedEnrollment)

                        .sourceAcademicSessionId(sourceSessionId)
                        .sourceClassRoomId(sourceClassId)
                        .sourceSectionId(sourceSectionId)

                        .targetAcademicSessionId(targetSession.getId())
                        .targetClassRoomId(targetClassRoom.getId())
                        .targetSectionId(targetSection.getId())

                        .transferDate(request.getTransferDate())
                        .reason(request.getReason())
                        .remarks(request.getRemarks())
                        .build();

        studentTransferRepository.save(transfer);


        return StudentTransferResponse.builder()
                .studentId(updatedEnrollment.getStudent().getId())
                .studentCode(updatedEnrollment.getStudent().getStudentCode())
                .admissionNo(updatedEnrollment.getStudent().getAdmissionNo())
                .studentName(buildStudentName(updatedEnrollment))

                .oldEnrollmentId(updatedEnrollment.getId())
                .newEnrollmentId(updatedEnrollment.getId())

                .sourceAcademicSessionId(sourceSessionId)
                .sourceAcademicSessionName(sourceSessionName)
                .sourceClassRoomId(sourceClassId)
                .sourceClassRoomName(sourceClassName)
                .sourceSectionId(sourceSectionId)
                .sourceSectionName(sourceSectionName)

                .targetAcademicSessionId(targetSession.getId())
                .targetAcademicSessionName(targetSession.getName())
                .targetClassRoomId(targetClassRoom.getId())
                .targetClassRoomName(targetClassRoom.getName())
                .targetSectionId(targetSection.getId())
                .targetSectionName(targetSection.getName())

                .transferDate(request.getTransferDate())
                .reason(request.getReason())
                .remarks(request.getRemarks())
                .status("TRANSFERRED")
                .build();
    }


    private StudentTransferResponse transferToDifferentSession(
            StudentEnrollment sourceEnrollment,
            AcademicSession targetSession,
            ClassRoom targetClassRoom,
            Section targetSection,
            StudentTransferRequest request) {

        // ---------------------------------------------------------
        // Check duplicate target enrollment
        // ---------------------------------------------------------

        boolean alreadyEnrolled =
                studentEnrollmentRepository
                        .existsByStudentIdAndAcademicSessionId(
                                request.getStudentId(),
                                targetSession.getId()
                        );

        if (alreadyEnrolled) {
            throw new IllegalArgumentException(
                    "Student already has an enrollment in the target academic session."
            );
        }


        // ---------------------------------------------------------
        // Mark source enrollment as TRANSFERRED
        // ---------------------------------------------------------

        sourceEnrollment.setEnrollmentStatus(
                EnrollmentStatus.TRANSFERRED
        );

        sourceEnrollment.setActive(false);

        studentEnrollmentRepository.save(sourceEnrollment);


        // ---------------------------------------------------------
        // Create target enrollment
        // ---------------------------------------------------------

        StudentEnrollment targetEnrollment =
                StudentEnrollment.builder()
                        .student(sourceEnrollment.getStudent())
                        .academicSession(targetSession)
                        .classRoom(targetClassRoom)
                        .section(targetSection)
                        .rollNo(sourceEnrollment.getRollNo())
                        .admissionDate(request.getTransferDate())
                        .enrollmentStatus(EnrollmentStatus.ACTIVE)
                        .active(true)
                        .build();

        targetEnrollment =
                studentEnrollmentRepository.save(targetEnrollment);


        // ---------------------------------------------------------
        // Create transfer history
        // ---------------------------------------------------------

        StudentTransfer transfer =
                StudentTransfer.builder()
                        .student(sourceEnrollment.getStudent())
                        .sourceEnrollment(sourceEnrollment)
                        .targetEnrollment(targetEnrollment)

                        .sourceAcademicSessionId(
                                sourceEnrollment.getAcademicSession().getId()
                        )
                        .sourceClassRoomId(
                                sourceEnrollment.getClassRoom().getId()
                        )
                        .sourceSectionId(
                                sourceEnrollment.getSection().getId()
                        )

                        .targetAcademicSessionId(
                                targetSession.getId()
                        )
                        .targetClassRoomId(
                                targetClassRoom.getId()
                        )
                        .targetSectionId(
                                targetSection.getId()
                        )

                        .transferDate(request.getTransferDate())
                        .reason(request.getReason())
                        .remarks(request.getRemarks())
                        .build();

        studentTransferRepository.save(transfer);


        return StudentTransferResponse.builder()
                .studentId(targetEnrollment.getStudent().getId())
                .studentCode(targetEnrollment.getStudent().getStudentCode())
                .admissionNo(targetEnrollment.getStudent().getAdmissionNo())
                .studentName(buildStudentName(targetEnrollment))

                .oldEnrollmentId(sourceEnrollment.getId())
                .newEnrollmentId(targetEnrollment.getId())

                .sourceAcademicSessionId(
                        sourceEnrollment.getAcademicSession().getId()
                )
                .sourceAcademicSessionName(
                        sourceEnrollment.getAcademicSession().getName()
                )
                .sourceClassRoomId(
                        sourceEnrollment.getClassRoom().getId()
                )
                .sourceClassRoomName(
                        sourceEnrollment.getClassRoom().getName()
                )
                .sourceSectionId(
                        sourceEnrollment.getSection().getId()
                )
                .sourceSectionName(
                        sourceEnrollment.getSection().getName()
                )

                .targetAcademicSessionId(targetSession.getId())
                .targetAcademicSessionName(targetSession.getName())
                .targetClassRoomId(targetClassRoom.getId())
                .targetClassRoomName(targetClassRoom.getName())
                .targetSectionId(targetSection.getId())
                .targetSectionName(targetSection.getName())

                .transferDate(request.getTransferDate())
                .reason(request.getReason())
                .remarks(request.getRemarks())
                .status("TRANSFERRED")
                .build();
    }


    private EligibleTransferStudentResponse toResponse(
            StudentEnrollment enrollment) {

        return EligibleTransferStudentResponse.builder()
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
                .classRoomId(enrollment.getClassRoom().getId())
                .classRoomName(enrollment.getClassRoom().getName())
                .sectionId(enrollment.getSection().getId())
                .sectionName(enrollment.getSection().getName())
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

        return (
                (firstName == null ? "" : firstName)
                        + " "
                        + (lastName == null ? "" : lastName)
        ).trim();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentTransferResponse getById(Long id) {

        StudentTransfer transfer =
                studentTransferRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Student transfer not found with id: " + id
                                )
                        );

        return toTransferResponse(transfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentTransferResponse> getByStudentId(
            Long studentId) {

        return studentTransferRepository
                .findByStudentIdOrderByTransferDateDesc(studentId)
                .stream()
                .map(this::toTransferResponse)
                .toList();
    }

    private StudentTransferResponse toTransferResponse(
            StudentTransfer transfer) {

        StudentEnrollment source =
                transfer.getSourceEnrollment();

        StudentEnrollment target =
                transfer.getTargetEnrollment();

        return StudentTransferResponse.builder()

                .studentId(
                        transfer.getStudent().getId()
                )

                .studentCode(
                        transfer.getStudent().getStudentCode()
                )

                .admissionNo(
                        transfer.getStudent().getAdmissionNo()
                )

                .studentName(
                        buildStudentName(source)
                )

                .oldEnrollmentId(
                        source != null
                                ? source.getId()
                                : null
                )

                .newEnrollmentId(
                        target != null
                                ? target.getId()
                                : null
                )

                .sourceAcademicSessionId(
                        transfer.getSourceAcademicSessionId()
                )

                .sourceAcademicSessionName(
                        source != null
                                ? source.getAcademicSession().getName()
                                : null
                )

                .sourceClassRoomId(
                        transfer.getSourceClassRoomId()
                )

                .sourceClassRoomName(
                        source != null
                                ? source.getClassRoom().getName()
                                : null
                )

                .sourceSectionId(
                        transfer.getSourceSectionId()
                )

                .sourceSectionName(
                        source != null
                                ? source.getSection().getName()
                                : null
                )

                .targetAcademicSessionId(
                        transfer.getTargetAcademicSessionId()
                )

                .targetAcademicSessionName(
                        target != null
                                ? target.getAcademicSession().getName()
                                : null
                )

                .targetClassRoomId(
                        transfer.getTargetClassRoomId()
                )

                .targetClassRoomName(
                        target != null
                                ? target.getClassRoom().getName()
                                : null
                )

                .targetSectionId(
                        transfer.getTargetSectionId()
                )

                .targetSectionName(
                        target != null
                                ? target.getSection().getName()
                                : null
                )

                .transferDate(
                        transfer.getTransferDate()
                )

                .reason(
                        transfer.getReason()
                )

                .remarks(
                        transfer.getRemarks()
                )

                .status("TRANSFERRED")

                .build();
    }
}