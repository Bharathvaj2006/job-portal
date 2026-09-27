package com.jobportal.controller;

import com.jobportal.dto.ResumeAnalysisResponse;
import com.jobportal.service.ResumeAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResumeAnalysisControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResumeAnalysisService resumeAnalysisService;

    @Test
    @WithMockUser(
            username = "candidate@example.com",
            roles = "CANDIDATE"
    )
    void candidateShouldBeAllowedToAnalyzeResume() throws Exception {

        when(resumeAnalysisService.analyzeResume(
                15L,
                "candidate@example.com"
        )).thenReturn(null);

        mockMvc.perform(
                post("/api/resume-analysis/jobs/15")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "recruiter@example.com",
            roles = "RECRUITER"
    )
    void recruiterShouldBeForbidden() throws Exception {

        mockMvc.perform(
                post("/api/resume-analysis/jobs/15")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void adminShouldBeForbidden() throws Exception {

        mockMvc.perform(
                post("/api/resume-analysis/jobs/15")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "candidate@example.com",
            roles = "CANDIDATE"
    )
    void candidateShouldReceiveAnalysisResponse() throws Exception {

        ResumeAnalysisResponse response =
                new ResumeAnalysisResponse(
                        15L,
                        "Java Backend Developer",
                        60.0,
                        java.util.List.of(
                                "java",
                                "spring boot",
                                "postgresql"
                        ),
                        java.util.List.of(
                                "docker",
                                "aws"
                        )
                );

        when(resumeAnalysisService.analyzeResume(
                15L,
                "candidate@example.com"
        )).thenReturn(response);

        mockMvc.perform(
                        post("/api/resume-analysis/jobs/15")
                )
                .andExpect(status().isOk())
                .andExpect(
                                jsonPath("$.jobId")
                                .value(15)
                )
                .andExpect(
                                jsonPath("$.jobTitle")
                                .value("Java Backend Developer")
                )
                .andExpect(
                                jsonPath("$.matchPercentage")
                                .value(60.0)
                )
                .andExpect(
                                jsonPath("$.matchedSkills[0]")
                                .value("java")
                )
                .andExpect(
                                jsonPath("$.matchedSkills[1]")
                                .value("spring boot")
                )
                .andExpect(
                                jsonPath("$.matchedSkills[2]")
                                .value("postgresql")
                )
                .andExpect(
                                jsonPath("$.missingSkills[0]")
                                .value("docker")
                )
                .andExpect(
                                jsonPath("$.missingSkills[1]")
                                .value("aws")
                );
    }
}