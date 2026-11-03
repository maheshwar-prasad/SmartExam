package com.smartexam.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single field validation error.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    /**
     * Field name.
     */
    private String field;

    /**
     * Validation message.
     */
    private String message;

}