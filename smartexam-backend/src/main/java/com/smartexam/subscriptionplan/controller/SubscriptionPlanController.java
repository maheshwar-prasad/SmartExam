package com.smartexam.subscriptionplan.controller;

import com.smartexam.subscriptionplan.dto.SubscriptionPlanRequest;
import com.smartexam.subscriptionplan.dto.SubscriptionPlanResponse;
import com.smartexam.subscriptionplan.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;


    /**
     * Create subscription plan.
     */
    @PostMapping
    public ResponseEntity<SubscriptionPlanResponse> create(
            @Valid @RequestBody SubscriptionPlanRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        subscriptionPlanService.create(request)
                );
    }


    /**
     * Get subscription plan by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subscriptionPlanService.getById(id)
        );
    }


    /**
     * Get all active subscription plans.
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanResponse>> getAll() {

        return ResponseEntity.ok(
                subscriptionPlanService.getAll()
        );
    }


    /**
     * Get paginated subscription plans.
     *
     * Example:
     *
     * GET /api/v1/subscription-plans/page?page=0&size=10
     */
    @GetMapping("/page")
    public ResponseEntity<Page<SubscriptionPlanResponse>> getPage(
            Pageable pageable) {

        return ResponseEntity.ok(
                subscriptionPlanService.getPage(
                        pageable
                )
        );
    }


    /**
     * Update subscription plan.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPlanRequest request) {

        return ResponseEntity.ok(
                subscriptionPlanService.update(
                        id,
                        request
                )
        );
    }


    /**
     * Soft delete subscription plan.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        subscriptionPlanService.delete(id);

        return ResponseEntity.noContent().build();
    }


    /**
     * Activate subscription plan.
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<SubscriptionPlanResponse> activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subscriptionPlanService.activate(id)
        );
    }


    /**
     * Deactivate subscription plan.
     */
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<SubscriptionPlanResponse> deactivate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subscriptionPlanService.deactivate(id)
        );
    }
}