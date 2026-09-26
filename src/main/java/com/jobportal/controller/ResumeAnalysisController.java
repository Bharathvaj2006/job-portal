package com.jobportal.controller;

import com.jobportal.dto.ResumeAnalysisResponse;
import com.jobportal.service.ResumeAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/resume-analysis")
public class ResumeAnalysisController {

    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeAnalysisController(
            ResumeAnalysisService resumeAnalysisService) {
        this.resumeAnalysisService = resumeAnalysisService;
    }

    @PostMapping("/jobs/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ResumeAnalysisResponse> analyzeResume(
            @PathVariable Long jobId,
            Principal principal) throws IOException {

        ResumeAnalysisResponse response =
                resumeAnalysisService.analyzeResume(
                        jobId,
                        principal.getName()
                );

        return ResponseEntity.ok(response);
    }
}