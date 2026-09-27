package com.jobportal.controller;

import com.jobportal.dto.ResumeAnalysisResponse;
import com.jobportal.service.ResumeAnalysisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeAnalysisControllerTest {

    @Mock
    private ResumeAnalysisService resumeAnalysisService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ResumeAnalysisController resumeAnalysisController;

    @Test
    void shouldAnalyzeResumeSuccessfully() throws Exception {

        Long jobId = 15L;
        String email = "candidate@example.com";

        ResumeAnalysisResponse response =
                new ResumeAnalysisResponse(
                        jobId,
                        "Java Backend Developer",
                        60.0,
                        List.of("java", "spring boot", "postgresql"),
                        List.of("docker", "aws")
                );

        when(principal.getName()).thenReturn(email);

        when(resumeAnalysisService.analyzeResume(
                jobId,
                email
        )).thenReturn(response);

        ResponseEntity<ResumeAnalysisResponse> result =
                resumeAnalysisController.analyzeResume(
                        jobId,
                        principal
                );

        assertEquals(200, result.getStatusCode().value());

        assertNotNull(result.getBody());

        assertEquals(jobId, result.getBody().getJobId());

        assertEquals(
                "Java Backend Developer",
                result.getBody().getJobTitle()
        );

        assertEquals(
                60.0,
                result.getBody().getMatchPercentage()
        );

        assertEquals(
                List.of("java", "spring boot", "postgresql"),
                result.getBody().getMatchedSkills()
        );

        assertEquals(
                List.of("docker", "aws"),
                result.getBody().getMissingSkills()
        );

        verify(principal).getName();

        verify(resumeAnalysisService)
                .analyzeResume(jobId, email);
    }
}