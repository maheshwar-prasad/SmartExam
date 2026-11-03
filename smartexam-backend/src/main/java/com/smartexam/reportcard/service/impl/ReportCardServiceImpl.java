package com.smartexam.reportcard.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.exam.entity.Exam;
import com.smartexam.exam.repository.ExamRepository;
import com.smartexam.examresult.entity.ExamResult;
import com.smartexam.examresult.repository.ExamResultRepository;

import com.smartexam.reportcard.dto.StudentReportCardResponse;
import com.smartexam.reportcard.service.ReportCardService;
import com.smartexam.student.entity.Student;
import com.smartexam.student.repository.StudentRepository;
import com.smartexam.marks.entity.StudentMarks;
import com.smartexam.marks.repository.StudentMarksRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportCardServiceImpl
        implements ReportCardService {

    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;
    private final StudentMarksRepository studentMarksRepository;

    public ReportCardServiceImpl(
            StudentRepository studentRepository,
            ExamRepository examRepository,
            ExamResultRepository examResultRepository,
            StudentMarksRepository studentMarksRepository) {

        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        this.examResultRepository = examResultRepository;
        this.studentMarksRepository = studentMarksRepository;
    }

    @Override
    public StudentReportCardResponse getReportCard(
            Long studentId,
            Long examId) {

        /*
         * Step 1:
         * Get student
         */
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: "
                                        + studentId
                        )
                );

        /*
         * Step 2:
         * Get exam
         */
        Exam exam = examRepository
                .findById(examId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Exam not found with id: "
                                        + examId
                        )
                );

        /*
         * Step 3:
         * Get generated exam result
         */
        ExamResult examResult =
                examResultRepository
                        .findByStudentIdAndExamId(
                                studentId,
                                examId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exam result not found"
                                )
                        );

        /*
         * Step 4:
         * Get subject-wise marks
         */
        List<StudentMarks> studentMarks =
                studentMarksRepository
                        .findByStudentIdAndExamIdAndActiveTrue(
                                studentId,
                                examId
                        );

        /*
         * Step 5:
         * Build subject-wise results
         */
        List<StudentReportCardResponse.SubjectResult>
                subjects = studentMarks.stream()
                .map(this::mapSubjectResult)
                .toList();

        /*
         * Step 6:
         * Build complete report card
         */
        return StudentReportCardResponse.builder()

                // Student Information
                .studentId(student.getId())
                .studentName(
                        buildStudentName(student)
                )
                .rollNumber(
                        student.getRollNo()
                )

                // Organization Information
                .organizationId(
                        exam.getOrganizationId()
                )

                // Exam Information
                .examId(exam.getId())
                .examName(exam.getName())
                .examType(String.valueOf(exam.getExamType()))

                // Academic Information
                .academicSessionId(
                        exam.getAcademicSessionId()
                )

                .classRoomId(
                        exam.getClassRoomId()
                )

                .sectionId(
                        exam.getSectionId()
                )

                // Subject Results
                .subjects(subjects)

                // Overall Result
                .totalSubjects(
                        examResult.getTotalSubjects()
                )

                .totalMaximumMarks(
                        examResult.getTotalMaximumMarks()
                )

                .totalObtainedMarks(
                        examResult.getTotalObtainedMarks()
                )

                .percentage(
                        examResult.getPercentage()
                )

                .grade(
                        examResult.getGrade()
                )

                .resultStatus(
                        examResult.getResultStatus()
                )

                .published(
                        examResult.getPublished()
                )

                .build();
    }

    /**
     * Convert StudentMarks to subject-wise report result.
     */
    private StudentReportCardResponse.SubjectResult
    mapSubjectResult(StudentMarks marks) {

        String subjectStatus =
                determineSubjectStatus(marks);

        return StudentReportCardResponse
                .SubjectResult
                .builder()

                /*
                 * Change subjectId if your StudentMarks
                 * entity uses a different field.
                 */
                .subjectId(
                        marks.getExamSubjectId()
                )

                .maximumMarks(
                        marks.getMaximumMarks()
                )

                .passMarks(
                        marks.getPassMarks()
                )

                .obtainedMarks(
                        marks.getObtainedMarks()
                )

                .subjectStatus(
                        subjectStatus
                )

                .remarks(
                        marks.getRemarks()
                )

                .build();
    }

    /**
     * Determine PASS / FAIL / INCOMPLETE
     * for an individual subject.
     */
    private String determineSubjectStatus(
            StudentMarks marks) {

        if (marks.getObtainedMarks() == null) {
            return "INCOMPLETE";
        }

        if (marks.getPassMarks() == null) {
            return "PENDING";
        }

        if (marks.getObtainedMarks()
                .compareTo(marks.getPassMarks()) < 0) {

            return "FAIL";
        }

        return "PASS";
    }

    /**
     * Build full student name.
     */
    private String buildStudentName(
            Student student) {

        String firstName =
                student.getFirstName() != null
                        ? student.getFirstName()
                        : "";

        String lastName =
                student.getLastName() != null
                        ? student.getLastName()
                        : "";

        return (firstName + " " + lastName).trim();
    }
}