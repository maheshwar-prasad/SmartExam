package com.smartexam.dashboard.service.impl;

import com.smartexam.attendance.repository.AttendanceRepository;
import com.smartexam.classroom.repository.ClassRoomRepository;
import com.smartexam.common.enums.ExamStatus;
import com.smartexam.dashboard.dto.*;
import com.smartexam.dashboard.service.DashboardService;
import com.smartexam.exam.repository.ExamRepository;
import com.smartexam.examresult.repository.ExamResultRepository;
import com.smartexam.organization.repository.OrganizationRepository;
import com.smartexam.section.repository.SectionRepository;
import com.smartexam.student.repository.StudentRepository;
import com.smartexam.subject.repository.SubjectRepository;
import com.smartexam.teacher.repository.TeacherRepository;
import com.smartexam.user.entity.User;
import com.smartexam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    private final ClassRoomRepository classRoomRepository;

    private final SectionRepository sectionRepository;

    private final SubjectRepository subjectRepository;

    private final ExamRepository examRepository;

    private final AttendanceRepository attendanceRepository;

    private final ExamResultRepository examResultRepository;


    // =========================================================
    // MAIN DASHBOARD ENTRY POINT
    // =========================================================

    @Override
    public DashboardSummaryResponse getSummary(DashboardFilterRequest filter) {

        User currentUser = getCurrentUser();

        String role = getCurrentUserRole(currentUser);

        return switch (role) {

            case "ADMIN" -> getAdminDashboard(filter);

            case "ORGANIZATION_ADMIN" -> getOrganizationAdminDashboard(currentUser, filter);

            case "TEACHER" -> getTeacherDashboard(currentUser, filter);

            case "STUDENT" -> getStudentDashboard(currentUser, filter);

            default -> throw new AccessDeniedException("Dashboard access is not configured for role: " + role);
        };
    }


    // =========================================================
    // ADMIN DASHBOARD
    // =========================================================

    private DashboardSummaryResponse getAdminDashboard(DashboardFilterRequest filter) {

        /*
         * ADMIN can view the complete system dashboard.
         *
         * If filters are provided, they are applied.
         *
         * For now, the summary uses the existing repository
         * methods. More advanced filtering can be introduced
         * later through DashboardQueryRepository.
         */

        return buildDashboardSummary(filter);
    }


    // =========================================================
    // ORGANIZATION ADMIN DASHBOARD
    // =========================================================

    private DashboardSummaryResponse getOrganizationAdminDashboard(User user, DashboardFilterRequest filter) {

        /*
         * Organization Admin must NEVER be allowed to access
         * another organization's dashboard by manipulating
         * organizationId in the request.
         */

        if (user.getOrganization() == null || user.getOrganization().getId() == null) {

            throw new IllegalStateException("User organization is not configured");
        }

        Long organizationId = user.getOrganization().getId();

        /*
         * Force organization scope from the authenticated user.
         */

        filter.setOrganizationId(organizationId);

        return buildDashboardSummary(filter);
    }


    // =========================================================
    // TEACHER DASHBOARD
    // =========================================================

    private DashboardSummaryResponse getTeacherDashboard(User user, DashboardFilterRequest filter) {

        /*
         * IMPORTANT:
         *
         * The current User entity only directly contains:
         *
         * User -> Organization
         * User -> Role
         *
         * The exact User -> Teacher relationship has not been
         * established here.
         *
         * Therefore we enforce organization scope for now.
         *
         * Once Teacher.user relationship/repository is confirmed,
         * this method should be restricted to the teacher's
         * assigned classes/sections/subjects.
         */

        if (user.getOrganization() == null || user.getOrganization().getId() == null) {

            throw new IllegalStateException("Teacher organization is not configured");
        }

        filter.setOrganizationId(user.getOrganization().getId());

        return buildDashboardSummary(filter);
    }


    // =========================================================
    // STUDENT DASHBOARD
    // =========================================================

    private DashboardSummaryResponse getStudentDashboard(User user, DashboardFilterRequest filter) {

        /*
         * IMPORTANT:
         *
         * Student-specific restriction requires:
         *
         * User -> Student
         *
         * The Student entity shown previously has:
         *
         * Student -> User
         *
         * but we have not yet added a repository method that
         * resolves Student from the authenticated User ID.
         *
         * Therefore, do NOT accept studentId from the client.
         *
         * For now we enforce organization scope.
         *
         * The next refinement will restrict this to the
         * authenticated student's own data.
         */

        if (user.getOrganization() == null || user.getOrganization().getId() == null) {

            throw new IllegalStateException("Student organization is not configured");
        }

        filter.setOrganizationId(user.getOrganization().getId());

        return buildDashboardSummary(filter);
    }


    // =========================================================
    // COMMON DASHBOARD SUMMARY
    // =========================================================

    private DashboardSummaryResponse buildDashboardSummary(DashboardFilterRequest filter) {

        if (filter == null) {
            filter = new DashboardFilterRequest();
        }

        /*
         * At this stage:
         *
         * - If no organization filter is supplied,
         *   global counts are returned.
         *
         * - If organizationId is supplied,
         *   organization-specific counts should be returned
         *   by the filtered repository queries.
         *
         * Do not use unfiltered count() for organization-scoped
         * requests.
         */

        boolean organizationFilter = filter.getOrganizationId() != null;

        long totalOrganizations = organizationFilter ? organizationRepository.countById(filter.getOrganizationId()) : organizationRepository.count();

        long activeOrganizations = organizationFilter ? organizationRepository.countByIdAndActiveTrue(filter.getOrganizationId()) : organizationRepository.countByActiveTrue();

        long totalStudents = organizationFilter ? studentRepository.countByOrganizationId(filter.getOrganizationId()) : studentRepository.count();

        long activeStudents = organizationFilter ? studentRepository.countActiveStudentsByFilter(filter.getOrganizationId(), filter.getClassRoomId(), filter.getSectionId()) : studentRepository.countByActiveTrue();

        long totalTeachers = teacherRepository.count();

        long activeTeachers = teacherRepository.countByActiveTrue();

        long totalClasses = classRoomRepository.count();

        long activeClasses = classRoomRepository.countByActiveTrue();

        long totalSections = sectionRepository.count();

        long activeSections = sectionRepository.countByActiveTrue();

        long totalSubjects = subjectRepository.count();

        long activeSubjects = subjectRepository.countByActiveTrue();

        long totalExams = examRepository.count();

        long activeExams = examRepository.countByActiveTrue();

        long scheduledExams = examRepository.countByActiveTrueAndStatus(ExamStatus.SCHEDULED);

        long inProgressExams = examRepository.countByActiveTrueAndStatus(ExamStatus.IN_PROGRESS);

        long completedExams = examRepository.countByActiveTrueAndStatus(ExamStatus.COMPLETED);

        return DashboardSummaryResponse.builder().totalOrganizations(totalOrganizations).activeOrganizations(activeOrganizations)

                .totalStudents(totalStudents).activeStudents(activeStudents)

                .totalTeachers(totalTeachers).activeTeachers(activeTeachers)

                .totalClasses(totalClasses).activeClasses(activeClasses)

                .totalSections(totalSections).activeSections(activeSections)

                .totalSubjects(totalSubjects).activeSubjects(activeSubjects)

                .totalExams(totalExams).activeExams(activeExams)

                .scheduledExams(scheduledExams).inProgressExams(inProgressExams).completedExams(completedExams)

                .build();
    }


    // =========================================================
    // STUDENT STATISTICS
    // =========================================================

    @Override
    public StudentStatisticsResponse getStudentStatistics() {

        long totalStudents = studentRepository.count();

        long activeStudents = studentRepository.countByActiveTrue();

        long inactiveStudents = totalStudents - activeStudents;

        List<StudentStatisticsResponse.ClassStudentCount> studentsByClass = studentRepository.countActiveStudentsByClass().stream().map(row -> StudentStatisticsResponse.ClassStudentCount.builder().classRoomId(((Number) row[0]).longValue()).className((String) row[1]).studentCount(((Number) row[2]).longValue()).build()).toList();

        List<StudentStatisticsResponse.SectionStudentCount> studentsBySection = studentRepository.countActiveStudentsBySection().stream().map(row -> StudentStatisticsResponse.SectionStudentCount.builder().sectionId(((Number) row[0]).longValue()).sectionName((String) row[1]).studentCount(((Number) row[2]).longValue()).build()).toList();

        return StudentStatisticsResponse.builder().totalStudents(totalStudents).activeStudents(activeStudents).inactiveStudents(inactiveStudents).studentsByClass(studentsByClass).studentsBySection(studentsBySection).build();
    }


    // =========================================================
    // ATTENDANCE STATISTICS
    // =========================================================

    @Override
    public AttendanceStatisticsResponse getAttendanceStatistics() {

        long totalRecords = attendanceRepository.countByActiveTrue();

        long present = attendanceRepository.countByActiveTrueAndStatus(com.smartexam.common.enums.AttendanceStatus.PRESENT);

        long absent = attendanceRepository.countByActiveTrueAndStatus(com.smartexam.common.enums.AttendanceStatus.ABSENT);

        long late = attendanceRepository.countByActiveTrueAndStatus(com.smartexam.common.enums.AttendanceStatus.LATE);

        double attendancePercentage = totalRecords == 0 ? 0.0 : ((double) present / totalRecords) * 100;

        attendancePercentage = Math.round(attendancePercentage * 100.0) / 100.0;

        return AttendanceStatisticsResponse.builder().totalRecords(totalRecords).present(present).absent(absent).late(late).attendancePercentage(attendancePercentage).build();
    }


    // =========================================================
    // EXAM STATISTICS
    // =========================================================

    @Override
    public ExamStatisticsResponse getExamStatistics() {

        LocalDate today = LocalDate.now();

        long totalExams = examRepository.count();

        long activeExams = examRepository.countByActiveTrue();

        long draftExams = examRepository.countByActiveTrueAndStatus(ExamStatus.DRAFT);

        long scheduledExams = examRepository.countByActiveTrueAndStatus(ExamStatus.SCHEDULED);

        long inProgressExams = examRepository.countByActiveTrueAndStatus(ExamStatus.IN_PROGRESS);

        long completedExams = examRepository.countByActiveTrueAndStatus(ExamStatus.COMPLETED);

        long cancelledExams = examRepository.countByActiveTrueAndStatus(ExamStatus.CANCELLED);

        long upcomingExams = examRepository.countByActiveTrueAndStartDateAfter(today);

        return ExamStatisticsResponse.builder().totalExams(totalExams).activeExams(activeExams).draftExams(draftExams).scheduledExams(scheduledExams).inProgressExams(inProgressExams).completedExams(completedExams).cancelledExams(cancelledExams).upcomingExams(upcomingExams).build();
    }


    // =========================================================
    // RESULT STATISTICS
    // =========================================================

    @Override
    public ResultStatisticsResponse getResultStatistics() {

        long totalResults = examResultRepository.count();

        long activeResults = examResultRepository.countByActiveTrue();

        long passedResults = examResultRepository.countByActiveTrueAndResultStatus("PASS");

        long failedResults = examResultRepository.countByActiveTrueAndResultStatus("FAIL");

        long pendingResults = examResultRepository.countByActiveTrueAndResultStatus("PENDING");

        double passPercentage = activeResults == 0 ? 0.0 : ((double) passedResults / activeResults) * 100;

        passPercentage = Math.round(passPercentage * 100.0) / 100.0;

        return ResultStatisticsResponse.builder().totalResults(totalResults).activeResults(activeResults).passedResults(passedResults).failedResults(failedResults).pendingResults(pendingResults).passPercentage(passPercentage).averagePercentage(examResultRepository.findAveragePercentage()).highestPercentage(examResultRepository.findHighestPercentage()).lowestPercentage(examResultRepository.findLowestPercentage()).build();
    }


    // =========================================================
    // CURRENT USER
    // =========================================================

    private User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new AccessDeniedException("User is not authenticated");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }


    // =========================================================
    // CURRENT USER ROLE
    // =========================================================

    private String getCurrentUserRole(User user) {

        if (user == null) {
            throw new IllegalArgumentException("Current user cannot be null");
        }

        if (user.getRole() == null || user.getRole().getName() == null || user.getRole().getName().isBlank()) {

            throw new IllegalStateException("User role is not configured");
        }

        return user.getRole().getName().trim().toUpperCase();
    }
    
}