package com.smartexam.common.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String field,
            String value) {

        super(field + " already exists : " + value);

    }

    public DuplicateResourceException(String message, String email, @NotBlank(message = "Organization email is required") @Email(message = "Invalid organization email") String requestEmail) {
        super(message);
    }

}