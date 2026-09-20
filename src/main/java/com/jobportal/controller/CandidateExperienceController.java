package com.jobportal.controller;

import com.jobportal.dto.CandidateExperienceRequest;
import com.jobportal.dto.ExperienceResponse;
import com.jobportal.entity.CandidateExperience;
import com.jobportal.service.CandidateExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/candidate/profile/experience")
public class CandidateExperienceController {

    private final CandidateExperienceService experienceService;

    public CandidateExperienceController(
            CandidateExperienceService experienceService) {

        this.experienceService = experienceService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateExperience> addExperience(
            @Valid @RequestBody CandidateExperienceRequest request,
            Principal principal) {

        CandidateExperience experience =
                experienceService.addExperience(
                        request,
                        principal.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(experience);
    }

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ExperienceResponse>> getMyExperience(
            Principal principal) {

        List<ExperienceResponse> experiences =
                experienceService.getMyExperience(
                        principal.getName());

        return ResponseEntity.ok(experiences);
    }

    @PutMapping("/{experienceId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long experienceId,
            @Valid @RequestBody CandidateExperienceRequest request,
            Principal principal) {

        ExperienceResponse experience =
                experienceService.updateExperience(
                        experienceId,
                        request,
                        principal.getName());

        return ResponseEntity.ok(experience);
    }

    @DeleteMapping("/{experienceId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> deleteExperience(
            @PathVariable Long experienceId,
            Principal principal) {

        experienceService.deleteExperience(
                experienceId,
                principal.getName());

        return ResponseEntity.noContent().build();
    }
}