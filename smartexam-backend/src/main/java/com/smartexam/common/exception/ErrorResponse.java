package com.smartexam.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Error timestamp.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * HTTP Status Code.
     */
    private int status;

    /**
     * HTTP Status Name.
     */
    private String error;

    /**
     * Business Error Message.
     */
    private String message;

    /**
     * API Path.
     */
    private String path;

    /**
     * Validation Errors.
     */
    @Builder.Default
    private List<ValidationError> validationErrors =
            new ArrayList<>();

}