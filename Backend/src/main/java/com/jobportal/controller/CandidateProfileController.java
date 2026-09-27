package com.jobportal.controller;

import com.jobportal.dto.CandidateProfileRequest;
import com.jobportal.dto.CandidateProfileResponse;
import com.jobportal.dto.CandidateSkillsRequest;
import com.jobportal.entity.CandidateProfile;
import com.jobportal.service.CandidateProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/candidate/profile")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    public CandidateProfileController(
            CandidateProfileService candidateProfileService) {

        this.candidateProfileService = candidateProfileService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfile> createProfile(
            @Valid @RequestBody CandidateProfileRequest request,
            Principal principal) {

        CandidateProfile profile =
                candidateProfileService.createProfile(
                        request,
                        principal.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profile);
    }

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> getMyProfile(
            Principal principal) {

        CandidateProfileResponse profile =
                candidateProfileService.getMyProfile(
                        principal.getName()
                );

        return ResponseEntity.ok(profile);
    }

    @PutMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> updateMyProfile(
            @Valid @RequestBody CandidateProfileRequest request,
            Principal principal) {

        CandidateProfileResponse profile =
                candidateProfileService.updateMyProfile(
                        request,
                        principal.getName()
                );

        return ResponseEntity.ok(profile);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> deleteMyProfile(Principal principal) {
        candidateProfileService.deleteProfile(principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/skills")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> updateSkills(
            @Valid @RequestBody CandidateSkillsRequest request,
            Principal principal) {

        CandidateProfileResponse profile =
                candidateProfileService.updateSkills(
                        request,
                        principal.getName()
                );

        return ResponseEntity.ok(profile);
    }
}