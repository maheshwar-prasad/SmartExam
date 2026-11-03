package com.smartexam.timetable.mapper;

import com.smartexam.timetable.dto.TimetableRequest;
import com.smartexam.timetable.dto.TimetableResponse;
import com.smartexam.timetable.entity.Timetable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface TimetableMapper {

    @Mapping(
            target = "teacherSubjectAssignment",
            ignore = true
    )
    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "active",
            ignore = true
    )
    Timetable toEntity(TimetableRequest request);

    @Mapping(
            target = "teacherSubjectAssignmentId",
            source = "teacherSubjectAssignment.id"
    )
    @Mapping(
            target = "teacherId",
            source = "teacherSubjectAssignment.teacher.id"
    )
    @Mapping(
            target = "teacherCode",
            source = "teacherSubjectAssignment.teacher.employeeCode"
    )
    @Mapping(
            target = "teacherName",
            expression = "java(getTeacherName(timetable))"
    )
    @Mapping(
            target = "subjectId",
            source = "teacherSubjectAssignment.subject.id"
    )
    @Mapping(
            target = "subjectCode",
            source = "teacherSubjectAssignment.subject.code"
    )
    @Mapping(
            target = "subjectName",
            source = "teacherSubjectAssignment.subject.name"
    )
    @Mapping(
            target = "academicSessionId",
            source = "teacherSubjectAssignment.academicSession.id"
    )
    @Mapping(
            target = "academicSessionName",
            source = "teacherSubjectAssignment.academicSession.name"
    )
    @Mapping(
            target = "classRoomId",
            source = "teacherSubjectAssignment.classRoom.id"
    )
    @Mapping(
            target = "classRoomCode",
            source = "teacherSubjectAssignment.classRoom.code"
    )
    @Mapping(
            target = "classRoomName",
            source = "teacherSubjectAssignment.classRoom.name"
    )
    @Mapping(
            target = "sectionId",
            source = "teacherSubjectAssignment.section.id"
    )
    @Mapping(
            target = "sectionCode",
            source = "teacherSubjectAssignment.section.code"
    )
    @Mapping(
            target = "sectionName",
            source = "teacherSubjectAssignment.section.name"
    )
    TimetableResponse toResponse(Timetable timetable);

    List<TimetableResponse> toResponseList(
            List<Timetable> timetables);

    default String getTeacherName(Timetable timetable) {

        if (timetable == null
                || timetable.getTeacherSubjectAssignment() == null
                || timetable.getTeacherSubjectAssignment().getTeacher() == null) {

            return null;
        }

        var teacher = timetable
                .getTeacherSubjectAssignment()
                .getTeacher();

        String firstName = teacher.getFirstName();
        String lastName = teacher.getLastName();

        if (firstName == null || firstName.isBlank()) {
            return lastName;
        }

        if (lastName == null || lastName.isBlank()) {
            return firstName;
        }

        return firstName + " " + lastName;
    }
}