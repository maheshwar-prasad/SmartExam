package com.smartexam.organization.exception;

public class OrganizationAlreadyExistsException extends RuntimeException {

    public OrganizationAlreadyExistsException(String field,
            String value) {

        super(field + " already exists : " + value);

    }

}