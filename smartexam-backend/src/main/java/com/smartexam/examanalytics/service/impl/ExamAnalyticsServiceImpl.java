package com.smartexam.examanalytics.service.impl;

import com.smartexam.common.exception.ResourceNotFoundException;
import com.smartexam.exam.entity.Exam;
import com.smartexam.exam.repository.ExamRepository;
import com.smartexam.examanalytics.dto.ExamAnalyticsResponse;
import com.smartexam.examanalytics.service.ExamAnalyticsService;
import com.smartexam.examresult.entity.ExamResult;
import com.smartexam.examresult.repository.ExamResultRepository;
import com.smartexam.student.entity.Student;
import com.smartexam.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ExamAnalyticsServiceImpl
        implements ExamAnalyticsService {

    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;
    private final StudentRepository studentRepository;

    public ExamAnalyticsServiceImpl(
            ExamRepository examRepository,
            ExamResultRepository examResultRepository,
            StudentRepository studentRepository) {

        this.examRepository = examRepository;
        this.examResultRepository = examResultRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Get complete analytics.
     */
    @Override
    public ExamAnalyticsResponse getExamAnalytics(
            Long examId) {

        Exam exam = getExam(examId);

        List<ExamResult> results =
                examResultRepository
                        .findByExamIdAndActiveTrue(examId);

        return buildAnalytics(exam, results);
    }

    /**
     * Get exam summary.
     */
    @Override
    public ExamAnalyticsResponse getExamSummary(
            Long examId) {

        Exam exam = getExam(examId);

        List<ExamResult> results =
                examResultRepository
                        .findByExamIdAndActiveTrue(examId);

        return ExamAnalyticsResponse.builder()
                .examId(exam.getId())
                .examName(exam.getName())
                .organizationId(exam.getOrganizationId())

                .totalStudents(results.size())
                .totalResults(results.size())

                .passedStudents(
                        (int) results.stream()
                                .filter(result ->
                                        "PASS".equalsIgnoreCase(
                                                result.getResultStatus()
                                        )
                                )
                                .count()
                )

                .failedStudents(
                        (int) results.stream()
                                .filter(result ->
                                        "FAIL".equalsIgnoreCase(
                                                result.getResultStatus()
                                        )
                                )
                                .count()
                )

                .incompleteStudents(
                        (int) results.stream()
                                .filter(result ->
                                        "INCOMPLETE".equalsIgnoreCase(
                                                result.getResultStatus()
                                        )
                                )
                                .count()
                )

                .passPercentage(
                        calculatePassPercentage(results)
                )

                .averagePercentage(
                        calculateAveragePercentage(results)
                )

                .highestPercentage(
                        calculateHighestPercentage(results)
                )

                .lowestPercentage(
                        calculateLowestPercentage(results)
                )

                .build();
    }

    /**
     * Grade distribution.
     */
    @Override
    public List<ExamAnalyticsResponse.GradeDistribution>
    getGradeDistribution(Long examId) {

        List<ExamResult> results =
                examResultRepository
                        .findByExamIdAndActiveTrue(examId);

        return buildGradeDistribution(results);
    }

    /**
     * Get top students.
     */
    @Override
    public List<ExamAnalyticsResponse.TopStudent>
    getTopStudents(
            Long examId,
            int limit) {

        List<ExamResult> results =
                examResultRepository
                        .findByExamIdAndActiveTrue(examId);

        return results.stream()

                .filter(result ->
                        "PASS".equalsIgnoreCase(
                                result.getResultStatus()
                        )
                )

                .sorted(
                        Comparator.comparing(
                                ExamResult::getPercentage,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )

                .limit(limit)

                .map(this::mapTopStudent)

                .toList();
    }


    /* =====================================================
       PRIVATE METHODS
       ===================================================== */

    private Exam getExam(Long examId) {

        return examRepository
                .findById(examId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Exam not found with id: "
                                        + examId
                        )
                );
    }

    private ExamAnalyticsResponse buildAnalytics(
            Exam exam,
            List<ExamResult> results) {

        return ExamAnalyticsResponse.builder()

                .examId(exam.getId())
                .examName(exam.getName())
                .organizationId(exam.getOrganizationId())

                .totalStudents(results.size())
                .totalResults(results.size())

                .passedStudents(
                        countByStatus(results, "PASS")
                )

                .failedStudents(
                        countByStatus(results, "FAIL")
                )

                .incompleteStudents(
                        countByStatus(results, "INCOMPLETE")
                )

                .passPercentage(
                        calculatePassPercentage(results)
                )

                .averagePercentage(
                        calculateAveragePercentage(results)
                )

                .highestPercentage(
                        calculateHighestPercentage(results)
                )

                .lowestPercentage(
                        calculateLowestPercentage(results)
                )

                .gradeDistribution(
                        buildGradeDistribution(results)
                )

                .topStudents(
                        getTopStudents(
                                exam.getId(),
                                10
                        )
                )

                .build();
    }

    private int countByStatus(
            List<ExamResult> results,
            String status) {

        return (int) results.stream()
                .filter(result ->
                        status.equalsIgnoreCase(
                                result.getResultStatus()
                        )
                )
                .count();
    }

    private BigDecimal calculatePassPercentage(
            List<ExamResult> results) {

        if (results.isEmpty()) {
            return BigDecimal.ZERO;
        }

        long passedCount =
                results.stream()
                        .filter(result ->
                                "PASS".equalsIgnoreCase(
                                        result.getResultStatus()
                                )
                        )
                        .count();

        return BigDecimal.valueOf(passedCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        BigDecimal.valueOf(results.size()),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private BigDecimal calculateAveragePercentage(
            List<ExamResult> results) {

        List<BigDecimal> percentages =
                results.stream()
                        .map(ExamResult::getPercentage)
                        .filter(value -> value != null)
                        .toList();

        if (percentages.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total =
                percentages.stream()
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return total.divide(
                BigDecimal.valueOf(percentages.size()),
                2,
                RoundingMode.HALF_UP
        );
    }

    private BigDecimal calculateHighestPercentage(
            List<ExamResult> results) {

        return results.stream()
                .map(ExamResult::getPercentage)
                .filter(value -> value != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateLowestPercentage(
            List<ExamResult> results) {

        return results.stream()
                .map(ExamResult::getPercentage)
                .filter(value -> value != null)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private List<ExamAnalyticsResponse.GradeDistribution>
    buildGradeDistribution(
            List<ExamResult> results) {

        if (results.isEmpty()) {
            return List.of();
        }

        Map<String, Long> gradeCounts =
                results.stream()
                        .filter(result ->
                                result.getGrade() != null
                        )
                        .collect(
                                Collectors.groupingBy(
                                        ExamResult::getGrade,
                                        Collectors.counting()
                                )
                        );

        return gradeCounts.entrySet()
                .stream()
                .sorted(
                        Map.Entry.comparingByKey()
                )
                .map(entry -> {

                    BigDecimal percentage =
                            BigDecimal.valueOf(
                                            entry.getValue()
                                    )
                                    .multiply(
                                            BigDecimal.valueOf(100)
                                    )
                                    .divide(
                                            BigDecimal.valueOf(
                                                    results.size()
                                            ),
                                            2,
                                            RoundingMode.HALF_UP
                                    );

                    return ExamAnalyticsResponse
                            .GradeDistribution
                            .builder()
                            .grade(entry.getKey())
                            .studentCount(entry.getValue())
                            .percentage(percentage)
                            .build();
                })
                .toList();
    }

    private ExamAnalyticsResponse.TopStudent
    mapTopStudent(
            ExamResult result) {

        Student student =
                studentRepository
                        .findById(
                                result.getStudentId()
                        )
                        .orElse(null);

        String studentName = "";

        String rollNumber = null;

        if (student != null) {

            studentName =
                    buildStudentName(student);

            rollNumber =
                    student.getRollNo();
        }

        return ExamAnalyticsResponse.TopStudent
                .builder()

                .studentId(
                        result.getStudentId()
                )

                .studentName(studentName)

                .rollNumber(rollNumber)

                .totalObtainedMarks(
                        result.getTotalObtainedMarks()
                )

                .totalMaximumMarks(
                        result.getTotalMaximumMarks()
                )

                .percentage(
                        result.getPercentage()
                )

                .grade(
                        result.getGrade()
                )

                .resultStatus(
                        result.getResultStatus()
                )

                .build();
    }

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

        return (firstName + " " + lastName)
                .trim();
    }
}