package com.jobportal.controller;

import com.jobportal.dto.CandidateEducationRequest;
import com.jobportal.entity.CandidateEducation;
import com.jobportal.service.CandidateEducationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.jobportal.dto.EducationResponse;
import java.util.List;

import java.security.Principal;

@RestController
@RequestMapping("/api/candidate/profile/education")
public class CandidateEducationController {

    private final CandidateEducationService educationService;

    public CandidateEducationController(
            CandidateEducationService educationService) {

        this.educationService = educationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateEducation> addEducation(
            @Valid @RequestBody CandidateEducationRequest request,
            Principal principal) {

        CandidateEducation education =
                educationService.addEducation(
                        request,
                        principal.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(education);
    }

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<EducationResponse>> getMyEducation(
            Principal principal) {

        List<EducationResponse> education =
                educationService.getMyEducation(principal.getName());

        return ResponseEntity.ok(education);
    }

    @PutMapping("/{educationId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<EducationResponse> updateEducation(
            @PathVariable Long educationId,
            @Valid @RequestBody CandidateEducationRequest request,
            Principal principal) {

        EducationResponse education =
                educationService.updateEducation(
                        educationId,
                        request,
                        principal.getName());

        return ResponseEntity.ok(education);
    }

    @DeleteMapping("/{educationId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> deleteEducation(
            @PathVariable Long educationId,
            Principal principal) {

        educationService.deleteEducation(
                educationId,
                principal.getName());

        return ResponseEntity.noContent().build();
    }
}