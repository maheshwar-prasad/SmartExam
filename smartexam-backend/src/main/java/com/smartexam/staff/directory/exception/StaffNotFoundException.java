package com.smartexam.staff.directory.exception;

public class StaffNotFoundException extends RuntimeException {

    public StaffNotFoundException(Long staffId, Long organizationId) {
        super(
                String.format(
                        "Staff not found with id %d for organization id %d",
                        staffId,
                        organizationId
                )
        );
    }
}