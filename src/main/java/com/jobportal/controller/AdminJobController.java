package com.jobportal.controller;

import com.jobportal.dto.AdminJobResponse;
import com.jobportal.dto.PageResponse;
import com.jobportal.service.AdminJobService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/jobs")
@Validated
public class AdminJobController {

    private final AdminJobService adminJobService;

    public AdminJobController(AdminJobService adminJobService) {
        this.adminJobService = adminJobService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<AdminJobResponse>> getAllJobs(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100")
            int size,
            @RequestParam(defaultValue = "id")
            String sortBy,
            @RequestParam(defaultValue = "asc")
            String direction) {

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'"
            );
        }

        if (!sortBy.equals("id")
                && !sortBy.equals("title")
                && !sortBy.equals("location")
                && !sortBy.equals("salary")
                && !sortBy.equals("createdAt")
                && !sortBy.equals("applicationDeadline")
                && !sortBy.equals("status")) {

            throw new IllegalArgumentException(
                    "Invalid sort field"
            );
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<AdminJobResponse> response = adminJobService.getAllJobs(pageable);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{jobId}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminJobResponse> closeJob(
            @PathVariable Long jobId) {

        AdminJobResponse response =
                adminJobService.closeJob(jobId);

        return ResponseEntity.ok(response);
    }
}