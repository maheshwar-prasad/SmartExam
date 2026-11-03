package com.smartexam.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiMessage {

    private boolean success;

    private String message;

}