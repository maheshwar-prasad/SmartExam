package com.smartexam.attendance.mapper;

import com.smartexam.attendance.dto.AttendanceRequest;
import com.smartexam.attendance.dto.AttendanceResponse;
import com.smartexam.attendance.dto.AttendanceUpdateRequest;
import com.smartexam.attendance.entity.Attendance;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AttendanceMapper {

    Attendance toEntity(
            AttendanceRequest request);

    @Mapping(target = "studentEnrollmentId",
            source = "studentEnrollment.id")

    @Mapping(target = "studentId",
            source = "studentEnrollment.student.id")

    @Mapping(target = "studentCode",
            source = "studentEnrollment.student.studentCode")

    @Mapping(target = "admissionNo",
            source = "studentEnrollment.student.admissionNo")

    @Mapping(target = "rollNo",
            source = "studentEnrollment.rollNo")

    @Mapping(target = "studentName",
            expression = "java(entity.getStudentEnrollment().getStudent().getFirstName() + \" \" + entity.getStudentEnrollment().getStudent().getLastName())")

    @Mapping(target = "teacherSubjectAssignmentId",
            source = "teacherSubjectAssignment.id")

    @Mapping(target = "teacherId",
            source = "teacherSubjectAssignment.teacher.id")

    @Mapping(
            target = "teacherCode",
            source = "teacherSubjectAssignment.teacher.employeeCode"
    )
    @Mapping(target = "teacherName",
            expression = "java(entity.getTeacherSubjectAssignment().getTeacher().getFirstName() + \" \" + entity.getTeacherSubjectAssignment().getTeacher().getLastName())")

    @Mapping(target = "subjectId",
            source = "teacherSubjectAssignment.subject.id")

    @Mapping(target = "subjectCode",
            source = "teacherSubjectAssignment.subject.code")

    @Mapping(target = "subjectName",
            source = "teacherSubjectAssignment.subject.name")

    @Mapping(target = "academicSessionId",
            source = "studentEnrollment.academicSession.id")

    @Mapping(target = "academicSessionName",
            source = "studentEnrollment.academicSession.name")

    @Mapping(target = "classRoomId",
            source = "studentEnrollment.classRoom.id")

    @Mapping(target = "classRoomCode",
            source = "studentEnrollment.classRoom.code")

    @Mapping(target = "classRoomName",
            source = "studentEnrollment.classRoom.name")

    @Mapping(target = "sectionId",
            source = "studentEnrollment.section.id")

    @Mapping(target = "sectionCode",
            source = "studentEnrollment.section.code")

    @Mapping(target = "sectionName",
            source = "studentEnrollment.section.name")

    AttendanceResponse toResponse(
            Attendance entity);

    List<AttendanceResponse> toResponseList(
            List<Attendance> entities);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            AttendanceUpdateRequest request,
            @MappingTarget Attendance entity);

}