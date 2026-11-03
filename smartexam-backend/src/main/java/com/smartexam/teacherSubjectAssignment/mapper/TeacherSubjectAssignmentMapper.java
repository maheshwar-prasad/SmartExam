package com.smartexam.teacherSubjectAssignment.mapper;

import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentRequest;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentResponse;
import com.smartexam.teacherSubjectAssignment.dto.TeacherSubjectAssignmentUpdateRequest;
import com.smartexam.teacherSubjectAssignment.entity.TeacherSubjectAssignment;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeacherSubjectAssignmentMapper {

    TeacherSubjectAssignment toEntity(
            TeacherSubjectAssignmentRequest request);

    @Mapping(target = "teacherId",
            source = "teacher.id")
    @Mapping(target = "employeeCode",
            source = "teacher.employeeCode")
    @Mapping(target = "teacherName",
            expression = "java(entity.getTeacher().getFirstName() + \" \" + entity.getTeacher().getLastName())")

    @Mapping(target = "subjectId",
            source = "subject.id")
    @Mapping(target = "subjectCode",
            source = "subject.code")
    @Mapping(target = "subjectName",
            source = "subject.name")

    @Mapping(target = "classRoomId",
            source = "classRoom.id")
    @Mapping(target = "classRoomCode",
            source = "classRoom.code")
    @Mapping(target = "classRoomName",
            source = "classRoom.name")

    @Mapping(target = "sectionId",
            source = "section.id")
    @Mapping(target = "sectionCode",
            source = "section.code")
    @Mapping(target = "sectionName",
            source = "section.name")

    @Mapping(target = "academicSessionId",
            source = "academicSession.id")
    @Mapping(target = "academicSessionName",
            source = "academicSession.name")
    TeacherSubjectAssignmentResponse toResponse(
            TeacherSubjectAssignment entity);

    List<TeacherSubjectAssignmentResponse> toResponseList(
            List<TeacherSubjectAssignment> entities);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(
            TeacherSubjectAssignmentUpdateRequest request,
            @MappingTarget TeacherSubjectAssignment entity);

}