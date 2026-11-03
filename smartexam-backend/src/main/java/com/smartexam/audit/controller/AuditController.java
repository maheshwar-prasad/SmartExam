package com.smartexam.audit.controller;

import com.smartexam.audit.dto.AuditLogResponse;
import com.smartexam.audit.service.AuditService;

import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;


    @GetMapping
    public ResponseEntity<@NotNull List<AuditLogResponse>>
    getAuditLogs() {

        return ResponseEntity.ok(
                auditService.getAuditLogs()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponse>
    getAuditLog(
            @PathVariable("id")
            Long id) {

        return ResponseEntity.ok(
                auditService.getAuditLog(id)
        );
    }
}