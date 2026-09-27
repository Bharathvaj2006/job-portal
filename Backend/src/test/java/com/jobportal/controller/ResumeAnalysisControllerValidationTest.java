package com.jobportal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jobportal.service.ResumeAnalysisService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResumeAnalysisControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResumeAnalysisService resumeAnalysisService;

    @Test
    @WithMockUser(
            username = "candidate@example.com",
            roles = "CANDIDATE"
    )
    void shouldRejectNonNumericJobId() throws Exception {

        mockMvc.perform(
                post("/api/resume-analysis/jobs/abc")
        ).andExpect(status().isBadRequest());
    }
}