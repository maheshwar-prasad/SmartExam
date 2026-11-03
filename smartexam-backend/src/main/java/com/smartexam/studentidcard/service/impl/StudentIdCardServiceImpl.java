package com.smartexam.studentidcard.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.security.service.CurrentUserService;
import com.smartexam.student.entity.Student;
import com.smartexam.student.repository.StudentRepository;
import com.smartexam.studentidcard.dto.*;
import com.smartexam.studentidcard.entity.StudentIdCard;
import com.smartexam.studentidcard.enums.IdCardStatus;
import com.smartexam.studentidcard.generator.StudentIdCardPdfGenerator;
import com.smartexam.studentidcard.repository.StudentIdCardRepository;
import com.smartexam.studentidcard.service.StudentIdCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StudentIdCardServiceImpl
        implements StudentIdCardService {

    private final StudentIdCardRepository studentIdCardRepository;

    private final StudentRepository studentRepository;

    private final CurrentUserService currentUserService;
    private final StudentIdCardPdfGenerator pdfGenerator;

    @Value("${smartexam.storage.student-id-cards.location}")
    private String storageLocation;


    @Override
    public StudentIdCardResponse generate(
            Long studentId,
            StudentIdCardGenerateRequest request) {

        Long organizationId =
                currentUserService.getOrganizationId();


        Student student =
                studentRepository
                        .findByIdAndActiveTrue(studentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found: "
                                                + studentId
                                ));


        if (
                student.getOrganization() == null
                        ||
                        !organizationId.equals(
                                student.getOrganization().getId()
                        )
        ) {

            throw new ResourceNotFoundException(
                    "Student not found: " + studentId
            );
        }


        if (
                request.getAcademicSessionId() != null
                        &&
                        studentIdCardRepository
                                .existsByStudentIdAndAcademicSessionIdAndActiveTrue(
                                        studentId,
                                        request.getAcademicSessionId()
                                )
        ) {

            throw new IllegalStateException(
                    "An active ID card already exists for "
                            + "this student and academic session"
            );
        }


        String cardNumber =
                generateCardNumber();


        StudentIdCard entity =
                StudentIdCard.builder()
                        .student(student)
                        .cardNumber(cardNumber)
                        .templateName(
                                request.getTemplateName()
                        )
                        .academicSessionId(
                                request.getAcademicSessionId()
                        )
                        .remarks(
                                request.getRemarks()
                        )
                        .status(
                                IdCardStatus.ACTIVE
                        )
                        .active(true)
                        .build();


        StudentIdCard saved =
                studentIdCardRepository.save(entity);


        // ============================================================
        // Generate PDF
        // ============================================================
        Path pdfPath = null;
        try {

            Path studentDirectory =
                    Paths.get(
                                    storageLocation,
                                    String.valueOf(studentId)
                            )
                            .toAbsolutePath()
                            .normalize();

            Files.createDirectories(studentDirectory);

            String fileName =
                    cardNumber + ".pdf";

            pdfPath =
                    studentDirectory
                            .resolve(fileName)
                            .normalize();

            if (!pdfPath.startsWith(studentDirectory)) {
                throw new IllegalStateException(
                        "Invalid ID card file path"
                );
            }

            log.info(
                    "Generating student ID card PDF. studentId={}, cardId={}, path={}",
                    studentId,
                    saved.getId(),
                    pdfPath
            );

            pdfGenerator.generate(
                    saved,
                    pdfPath
            );

            saved.setFileName(fileName);
            saved.setStoragePath(pdfPath.toString());

            saved =
                    studentIdCardRepository.save(saved);

        } catch (Exception ex) {

            log.error(
                    "Failed to generate ID card PDF. studentId={}, outputPath={}",
                    studentId,
                    pdfPath,
                    ex
            );

            throw new IllegalStateException(
                    "Unable to generate student ID card PDF",
                    ex
            );
        }


        return toResponse(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public StudentIdCardResponse getById(
            Long id) {

        StudentIdCard entity =
                studentIdCardRepository
                        .findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student ID card not found: "
                                                + id
                                ));


        validateOrganization(entity);

        return toResponse(entity);
    }


    @Override
    @Transactional(readOnly = true)
    public List<StudentIdCardResponse> getByStudentId(
            Long studentId) {

        Long organizationId =
                currentUserService.getOrganizationId();


        Student student =
                studentRepository
                        .findByIdAndActiveTrue(studentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found: "
                                                + studentId
                                ));


        if (
                student.getOrganization() == null
                        ||
                        !organizationId.equals(
                                student.getOrganization().getId()
                        )
        ) {

            throw new ResourceNotFoundException(
                    "Student not found: " + studentId
            );
        }


        return studentIdCardRepository
                .findByStudentIdAndActiveTrue(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
    public StudentIdCardBulkResponse generateBulk(
            StudentIdCardBulkRequest request) {

        List<BulkStudentIdCardResult> results =
                new ArrayList<>();

        int success = 0;
        int failed = 0;

        for (Long studentId : request.getStudentIds()) {

            try {

                // ============================================
                // Find active student
                // ============================================

                Student student =
                        studentRepository
                                .findByIdAndActiveTrue(studentId)
                                .orElseThrow(
                                        () -> new ResourceNotFoundException(
                                                "Student not found: " + studentId
                                        )
                                );


                // ============================================
                // Generate individual ID card
                // ============================================

                StudentIdCardResponse response =
                        generate(
                                studentId,
                                buildGenerateRequest(request)
                        );


                // ============================================
                // SUCCESS
                // ============================================

                success++;

                results.add(
                        BulkStudentIdCardResult.builder()
                                .studentId(studentId)
                                .studentName(
                                        buildStudentName(student)
                                )
                                .status("SUCCESS")
                                .message(
                                        "ID card generated successfully"
                                )
                                .cardId(response.getId())
                                .cardNumber(response.getCardNumber())
                                .build()
                );


            } catch (Exception ex) {

                // ============================================
                // FAILURE - DO NOT STOP BULK PROCESS
                // ============================================

                failed++;

                results.add(
                        BulkStudentIdCardResult.builder()
                                .studentId(studentId)
                                .status("FAILED")
                                .message(
                                        getSafeErrorMessage(ex)
                                )
                                .build()
                );
            }
        }


        // ================================================
        // Final response
        // ================================================

        return StudentIdCardBulkResponse.builder()
                .total(request.getStudentIds().size())
                .success(success)
                .failed(failed)
                .results(results)
                .build();
    }


    @Override
    public void activate(Long id) {

        StudentIdCard entity =
                getEntityForStatusChange(id);

        entity.setActive(true);
        entity.setStatus(IdCardStatus.ACTIVE);

        studentIdCardRepository.save(entity);
    }


    @Override
    public void deactivate(Long id) {

        StudentIdCard entity =
                getEntityForStatusChange(id);

        entity.setActive(false);
        entity.setStatus(IdCardStatus.REVOKED);

        studentIdCardRepository.save(entity);
    }


    private StudentIdCard getEntityForStatusChange(
            Long id) {

        StudentIdCard entity =
                studentIdCardRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student ID card not found: "
                                                + id
                                ));

        validateOrganization(entity);

        return entity;
    }


    private void validateOrganization(
            StudentIdCard entity) {

        Long organizationId =
                currentUserService.getOrganizationId();


        Student student =
                entity.getStudent();


        if (
                student == null
                        ||
                        student.getOrganization() == null
                        ||
                        !organizationId.equals(
                                student.getOrganization().getId()
                        )
        ) {

            throw new ResourceNotFoundException(
                    "Student ID card not found"
            );
        }
    }


    private String generateCardNumber() {

        return "ID-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }


    private StudentIdCardResponse toResponse(
            StudentIdCard entity) {

        Student student =
                entity.getStudent();


        String studentName =
                student.getFirstName()
                        + " "
                        + (
                        student.getLastName() == null
                                ? ""
                                : student.getLastName()
                );


        return StudentIdCardResponse.builder()

                .id(entity.getId())

                .studentId(student.getId())

                .studentName(studentName.trim())

                .studentCode(student.getStudentCode())

                .admissionNo(student.getAdmissionNo())

                .rollNo(student.getRollNo())

                .cardNumber(entity.getCardNumber())

                .templateName(entity.getTemplateName())

                .academicSessionId(
                        entity.getAcademicSessionId()
                )

                .status(entity.getStatus())

                .fileName(entity.getFileName())

                .remarks(entity.getRemarks())

                .active(entity.getActive())

                .createdAt(
                        entity.getCreatedAt() == null
                                ? null
                                : entity.getCreatedAt().toString()
                )

                .updatedAt(
                        entity.getUpdatedAt() == null
                                ? null
                                : entity.getUpdatedAt().toString()
                )

                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource download(Long id) {

        StudentIdCard entity =
                studentIdCardRepository
                        .findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student ID card not found: "
                                                + id
                                ));


        validateOrganization(entity);


        if (
                entity.getStoragePath() == null
                        ||
                        entity.getStoragePath().isBlank()
        ) {

            throw new ResourceNotFoundException(
                    "ID card PDF is not available"
            );
        }


        try {

            Path filePath =
                    Paths.get(
                                    entity.getStoragePath()
                            )
                            .toAbsolutePath()
                            .normalize();


            Path basePath =
                    Paths.get(
                                    storageLocation
                            )
                            .toAbsolutePath()
                            .normalize();


            if (!filePath.startsWith(basePath)) {

                throw new IllegalStateException(
                        "Invalid ID card storage path"
                );
            }


            if (!Files.exists(filePath)) {

                throw new ResourceNotFoundException(
                        "ID card file not found"
                );
            }


            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );


            if (
                    !resource.exists()
                            ||
                            !resource.isReadable()
            ) {

                throw new ResourceNotFoundException(
                        "ID card file is not readable"
                );
            }


            return resource;

        } catch (ResourceNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Unable to download student ID card",
                    ex
            );
        }
    }

    private StudentIdCardGenerateRequest buildGenerateRequest(
            StudentIdCardBulkRequest request) {

        return StudentIdCardGenerateRequest.builder()
                .academicSessionId(
                        request.getAcademicSessionId()
                )
                .templateName(
                        request.getTemplateName()
                )
                .remarks(
                        request.getRemarks()
                )
                .build();
    }

    private String getSafeErrorMessage(
            Exception ex) {

        if (ex.getMessage() == null ||
                ex.getMessage().isBlank()) {

            return "Unable to generate ID card";
        }

        return ex.getMessage();
    }

    private String buildStudentName(
            Student student) {

        String firstName =
                student.getFirstName() == null
                        ? ""
                        : student.getFirstName();

        String lastName =
                student.getLastName() == null
                        ? ""
                        : student.getLastName();

        return (
                firstName + " " + lastName
        ).trim();
    }
}