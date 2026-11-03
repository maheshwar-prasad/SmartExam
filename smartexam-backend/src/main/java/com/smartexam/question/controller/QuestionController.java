package com.smartexam.question.controller;

import com.smartexam.question.dto.QuestionRequest;
import com.smartexam.question.dto.QuestionResponse;
import com.smartexam.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> create(
            @Valid @RequestBody QuestionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(questionService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                questionService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAll() {

        return ResponseEntity.ok(
                questionService.getAll()
        );
    }

    @GetMapping("/page")
    public ResponseEntity<Page<QuestionResponse>> getAll(
            Pageable pageable) {

        return ResponseEntity.ok(
                questionService.getAll(pageable)
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<QuestionResponse>> getBySubject(
            @PathVariable Long subjectId) {

        return ResponseEntity.ok(
                questionService.getBySubject(subjectId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest request) {

        return ResponseEntity.ok(
                questionService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id) {

        questionService.deactivate(id);

        return ResponseEntity.noContent().build();
    }
}