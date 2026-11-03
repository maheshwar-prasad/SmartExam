package com.smartexam.classsubject.mapper;

import com.smartexam.classsubject.dto.ClassSubjectResponse;
import com.smartexam.classsubject.entity.ClassSubject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassSubjectMapper {

    @Mapping(
            target = "organizationId",
            source = "organization.id"
    )
    @Mapping(
            target = "organizationName",
            source = "organization.name"
    )
    @Mapping(
            target = "academicSessionId",
            source = "academicSession.id"
    )
    @Mapping(
            target = "academicSessionName",
            source = "academicSession.name"
    )
    @Mapping(
            target = "classRoomId",
            source = "classRoom.id"
    )
    @Mapping(
            target = "classRoomName",
            source = "classRoom.name"
    )
    @Mapping(
            target = "subjectId",
            source = "subject.id"
    )
    @Mapping(
            target = "subjectName",
            source = "subject.name"
    )
    @Mapping(
            target = "subjectCode",
            source = "subject.code"
    )
    ClassSubjectResponse toResponse(ClassSubject entity);
}