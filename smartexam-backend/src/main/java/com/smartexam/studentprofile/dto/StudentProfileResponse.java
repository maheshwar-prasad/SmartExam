package com.smartexam.studentprofile.dto;

import com.smartexam.common.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileResponse {

    private StudentInfo student;

    private CurrentEnrollment currentEnrollment;

    @Builder.Default
    private List<EnrollmentHistory> enrollmentHistory = List.of();

    @Builder.Default
    private List<TransferHistory> transferHistory = List.of();

    // =========================================================
    // Student Information
    // =========================================================

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentInfo {

        private Long id;

        private String studentCode;

        private String admissionNo;

        private String rollNo;

        private LocalDate admissionDate;

        private String firstName;

        private String lastName;

        private LocalDate dateOfBirth;

        private String gender;

        private String bloodGroup;

        private String fatherName;

        private String motherName;

        private String guardianName;

        private String mobile;

        private String email;

        private String address;

        private String photoUrl;

        private Long organizationId;

        private String organizationName;

        private Boolean active;
    }

    // =========================================================
    // Current Enrollment
    // =========================================================

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentEnrollment {

        private Long enrollmentId;

        private Long academicSessionId;

        private String academicSessionName;

        private Long classRoomId;

        private String classRoomName;

        private Long sectionId;

        private String sectionName;

        private String rollNo;

        private LocalDate admissionDate;

        private EnrollmentStatus enrollmentStatus;

        private Boolean active;
    }

    // =========================================================
    // Enrollment History
    // =========================================================

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentHistory {

        private Long enrollmentId;

        private Long academicSessionId;

        private String academicSessionName;

        private Long classRoomId;

        private String classRoomName;

        private Long sectionId;

        private String sectionName;

        private String rollNo;

        private LocalDate admissionDate;

        private EnrollmentStatus enrollmentStatus;

        private Boolean active;
    }

    // =========================================================
    // Transfer History
    // =========================================================

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferHistory {

        private Long transferId;

        private Long sourceAcademicSessionId;

        private String sourceAcademicSessionName;

        private Long sourceClassRoomId;

        private String sourceClassRoomName;

        private Long sourceSectionId;

        private String sourceSectionName;

        private Long targetAcademicSessionId;

        private String targetAcademicSessionName;

        private Long targetClassRoomId;

        private String targetClassRoomName;

        private Long targetSectionId;

        private String targetSectionName;

        private LocalDate transferDate;

        private String reason;

        private String remarks;
    }
}