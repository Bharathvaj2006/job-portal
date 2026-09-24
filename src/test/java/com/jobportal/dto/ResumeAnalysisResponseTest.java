package com.jobportal.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResumeAnalysisResponseTest {

    @Test
    void shouldCreateResumeAnalysisResponse() {

        ResumeAnalysisResponse response =
                new ResumeAnalysisResponse(
                        15L,
                        "Java Backend Developer",
                        80.0,
                        List.of(
                                "java",
                                "spring boot",
                                "postgresql",
                                "docker"
                        ),
                        List.of("aws")
                );

        assertEquals(
                15L,
                response.getJobId()
        );

        assertEquals(
                "Java Backend Developer",
                response.getJobTitle()
        );

        assertEquals(
                80.0,
                response.getMatchPercentage()
        );

        assertEquals(
                List.of(
                        "java",
                        "spring boot",
                        "postgresql",
                        "docker"
                ),
                response.getMatchedSkills()
        );

        assertEquals(
                List.of("aws"),
                response.getMissingSkills()
        );
    }

    @Test
    void shouldSetAndGetValues() {

        ResumeAnalysisResponse response =
                new ResumeAnalysisResponse();

        response.setJobId(20L);
        response.setJobTitle(
                "Full Stack Developer"
        );
        response.setMatchPercentage(75.50);

        response.setMatchedSkills(
                List.of(
                        "java",
                        "react"
                )
        );

        response.setMissingSkills(
                List.of(
                        "aws",
                        "docker"
                )
        );

        assertEquals(
                20L,
                response.getJobId()
        );

        assertEquals(
                "Full Stack Developer",
                response.getJobTitle()
        );

        assertEquals(
                75.50,
                response.getMatchPercentage()
        );

        assertEquals(
                List.of(
                        "java",
                        "react"
                ),
                response.getMatchedSkills()
        );

        assertEquals(
                List.of(
                        "aws",
                        "docker"
                ),
                response.getMissingSkills()
        );
    }
}