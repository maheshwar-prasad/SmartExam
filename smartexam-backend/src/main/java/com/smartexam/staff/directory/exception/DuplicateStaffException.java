package com.smartexam.staff.directory.exception;

public class DuplicateStaffException extends RuntimeException {

    public DuplicateStaffException(String field, String value) {
        super(
                String.format(
                        "Staff already exists with %s: %s",
                        field,
                        value
                )
        );
    }
}