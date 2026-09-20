package com.jobportal.controller;

import com.jobportal.dto.AdminApplicationResponse;
import com.jobportal.dto.AdminApplicationStatusRequest;
import com.jobportal.dto.PageResponse;
import com.jobportal.service.AdminApplicationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/applications")
@Validated
public class AdminApplicationController {

    private final AdminApplicationService adminApplicationService;

    public AdminApplicationController(
            AdminApplicationService adminApplicationService) {

        this.adminApplicationService = adminApplicationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<AdminApplicationResponse>> getAllApplications(
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
                && !sortBy.equals("status")
                && !sortBy.equals("appliedAt")
                && !sortBy.equals("updatedAt")) {

            throw new IllegalArgumentException(
                    "Invalid sort field"
            );
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        PageResponse<AdminApplicationResponse> response =
                adminApplicationService.getAllApplications(pageable);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody AdminApplicationStatusRequest request,
            Authentication authentication) {

        AdminApplicationResponse response =
                adminApplicationService.updateApplicationStatus(
                        applicationId,
                        request.getStatus(),
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }
}