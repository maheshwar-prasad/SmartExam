package com.smartexam.common.controller;

import com.smartexam.common.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<String> health(){

        return ApiResponse.<String>builder()
                .success(true)
                .message("Application Running")
                .data("SmartExam Backend Started Successfully")
                .build();

    }

}