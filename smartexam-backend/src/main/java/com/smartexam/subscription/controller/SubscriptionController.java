package com.smartexam.subscription.controller;

import com.smartexam.subscription.dto.SubscriptionRequest;
import com.smartexam.subscription.dto.SubscriptionResponse;
import com.smartexam.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(
            @Valid
            @RequestBody
            SubscriptionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        subscriptionService.create(
                                request
                        )
                );
    }


    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse>
    getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subscriptionService.getById(id)
        );
    }


    @GetMapping("/mine")
    public ResponseEntity<List<SubscriptionResponse>>
    getMine() {

        return ResponseEntity.ok(
                subscriptionService
                        .getMySubscriptions()
        );
    }


    @GetMapping("/organization")
    public ResponseEntity<Page<SubscriptionResponse>>
    getOrganizationSubscriptions(
            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        return ResponseEntity.ok(
                subscriptionService
                        .getOrganizationSubscriptions(
                                pageable
                        )
        );
    }


    @PutMapping("/{id}/activate")
    public ResponseEntity<SubscriptionResponse>
    activate(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subscriptionService.activate(id)
        );
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponse>
    cancel(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subscriptionService.cancel(id)
        );
    }


    @PutMapping("/{id}/expire")
    public ResponseEntity<SubscriptionResponse>
    expire(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subscriptionService.expire(id)
        );
    }
}