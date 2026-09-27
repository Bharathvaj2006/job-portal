package com.jobportal.controller;

import com.jobportal.dto.*;
import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.JobApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;

    public JobApplicationController(
            JobApplicationService jobApplicationService,
            UserRepository userRepository) {

        this.jobApplicationService = jobApplicationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<JobApplicationResponse> applyForJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobApplicationRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        JobApplicationResponse response =
                jobApplicationService.applyForJob(
                        jobId,
                        request,
                        candidate
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<PageResponse<JobApplicationResponse>> getMyApplications(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size cannot exceed 100")
            int size,
            @RequestParam(defaultValue = "appliedAt")
            String sortBy,
            @RequestParam(defaultValue = "desc")
            String direction,
            Authentication authentication) {

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'"
            );
        }

        if (!sortBy.equals("id")
                && !sortBy.equals("appliedAt")
                && !sortBy.equals("updatedAt")
                && !sortBy.equals("status")) {

            throw new IllegalArgumentException(
                    "Invalid sort field"
            );
        }
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);
        String email = authentication.getName();

        User candidate = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        ));
        PageResponse<JobApplicationResponse> applications =
                jobApplicationService.getMyApplications(
                        candidate,
                        pageable
                );

        return ResponseEntity.ok(applications);
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<PageResponse<JobApplicationResponse>> getRecruiterApplications(
            @RequestParam(defaultValue = "0")
            @Min(value = 0,message = "Page number cannot be negative")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size cannot exceed 100")
            int size,
            @RequestParam(defaultValue = "appliedAt")
            String sortBy,
            @RequestParam(defaultValue = "desc")
            String direction,
            Authentication authentication) {

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'"
            );
        }

        if (!sortBy.equals("id")
                && !sortBy.equals("appliedAt")
                && !sortBy.equals("updatedAt")
                && !sortBy.equals("status")) {

            throw new IllegalArgumentException(
                    "Invalid sort field"
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        String email = authentication.getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        ));

        PageResponse<JobApplicationResponse> applications =
                jobApplicationService
                        .getRecruiterApplications(
                                recruiter,
                                pageable
                        );

        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User recruiter = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        ));

        JobApplicationResponse response =
                jobApplicationService.updateApplicationStatus(
                        applicationId,
                        request.getStatus(),
                        recruiter
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{applicationId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<JobApplicationResponse> withdrawApplication(
            @PathVariable Long applicationId,
            Authentication authentication) {

        String email = authentication.getName();

        User candidate = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        ));

        JobApplicationResponse response =
                jobApplicationService.withdrawApplication(
                        applicationId,
                        candidate
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{applicationId}/history")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'RECRUITER')")
    public ResponseEntity<PageResponse<ApplicationStatusHistoryResponse>> getApplicationHistory(
            @PathVariable Long applicationId,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size cannot exceed 100")
            int size,
            @RequestParam(defaultValue = "changedAt")
            String sortBy,
            @RequestParam(defaultValue = "asc")
            String direction,
            Authentication authentication) {

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'"
            );
        }

        if (!sortBy.equals("id")
                && !sortBy.equals("changedAt")
                && !sortBy.equals("oldStatus")
                && !sortBy.equals("newStatus")) {

            throw new IllegalArgumentException(
                    "Invalid sort field"
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        ));

        PageResponse<ApplicationStatusHistoryResponse> history = jobApplicationService.getApplicationHistory(
                        applicationId,
                        user,
                        pageable
                );

        return ResponseEntity.ok(history);
    }
}