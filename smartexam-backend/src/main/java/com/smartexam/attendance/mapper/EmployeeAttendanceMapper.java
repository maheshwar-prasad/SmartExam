package com.smartexam.attendance.mapper;

import com.smartexam.attendance.dto.EmployeeAttendanceResponse;
import com.smartexam.attendance.entity.EmployeeAttendance;
import org.springframework.stereotype.Component;

@Component
public class EmployeeAttendanceMapper {

    public EmployeeAttendanceResponse toResponse(
            EmployeeAttendance attendance) {

        if (attendance == null) {
            return null;
        }

        var employee = attendance.getEmployee();

        return EmployeeAttendanceResponse.builder()
                .id(attendance.getId())
                .employeeId(employee.getId())
                .organizationId(
                        attendance.getOrganization().getId())
                .employeeCode(employee.getEmployeeCode())
                .employeeName(
                        employee.getFirstName() + " "
                                + employee.getLastName())
                .employeeType(
                        employee.getEmployeeType().name())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .remarks(attendance.getRemarks())
                .createdAt(attendance.getCreatedAt())
                .updatedAt(attendance.getUpdatedAt())
                .build();
    }
}