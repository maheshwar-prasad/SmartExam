package com.smartexam.institution.program.exception;

public class ProgramNotFoundException
        extends RuntimeException {

    public ProgramNotFoundException(Long id) {
        super(
                "Program not found with ID: " + id
        );
    }
}