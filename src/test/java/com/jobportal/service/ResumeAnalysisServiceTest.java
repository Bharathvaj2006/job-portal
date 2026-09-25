package com.jobportal.service;

import com.jobportal.dto.ResumeAnalysisResponse;
import com.jobportal.entity.CandidateResume;
import com.jobportal.entity.Job;
import com.jobportal.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeAnalysisServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CandidateResumeService candidateResumeService;

    @Mock
    private ResumeTextExtractor resumeTextExtractor;

    @Mock
    private SkillParser skillParser;

    @Mock
    private SkillMatcher skillMatcher;

    @Mock
    private SkillMatchCalculator skillMatchCalculator;

    @Mock
    private SkillGapAnalyzer skillGapAnalyzer;

    @InjectMocks
    private ResumeAnalysisService resumeAnalysisService;

    @Test
    void shouldAnalyzeResumeSuccessfully()
            throws Exception {

        Long jobId = 15L;
        String email = "candidate@example.com";

        Job job = new Job();

        job.setRequiredSkills(
                "Java, Spring Boot, PostgreSQL, Docker, AWS"
        );

        job.setTitle(
                "Java Backend Developer"
        );

        CandidateResume resume =
                new CandidateResume(
                        "resume.pdf",
                        "application/pdf",
                        1000L,
                        "uploads/resume.pdf",
                        LocalDateTime.now(),
                        null
                );

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql",
                        "docker",
                        "aws"
                );

        List<String> matchedSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql",
                        "docker"
                );

        List<String> missingSkills =
                List.of("aws");

        when(jobRepository.findById(jobId))
                .thenReturn(Optional.of(job));

        when(candidateResumeService
                .getMyResumeEntity(email))
                .thenReturn(resume);

        when(resumeTextExtractor
                .extractText(resume))
                .thenReturn(
                        "Java Spring Boot PostgreSQL Docker"
                );

        when(skillParser.parseRequiredSkills(
                job.getRequiredSkills()))
                .thenReturn(requiredSkills);

        when(skillMatcher.findMatchedSkills(
                "Java Spring Boot PostgreSQL Docker",
                requiredSkills))
                .thenReturn(matchedSkills);

        when(skillMatchCalculator
                .calculateMatchPercentage(
                        requiredSkills,
                        matchedSkills))
                .thenReturn(80.0);

        when(skillGapAnalyzer.findMissingSkills(
                requiredSkills,
                matchedSkills))
                .thenReturn(missingSkills);

        ResumeAnalysisResponse response =
                resumeAnalysisService.analyzeResume(
                        jobId,
                        email
                );

        assertEquals(
                job.getId(),
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
                matchedSkills,
                response.getMatchedSkills()
        );

        assertEquals(
                missingSkills,
                response.getMissingSkills()
        );

        verify(jobRepository)
                .findById(jobId);

        verify(candidateResumeService)
                .getMyResumeEntity(email);

        verify(resumeTextExtractor)
                .extractText(resume);

        verify(skillParser)
                .parseRequiredSkills(
                        job.getRequiredSkills()
                );

        verify(skillMatcher)
                .findMatchedSkills(
                        "Java Spring Boot PostgreSQL Docker",
                        requiredSkills
                );

        verify(skillMatchCalculator)
                .calculateMatchPercentage(
                        requiredSkills,
                        matchedSkills
                );

        verify(skillGapAnalyzer)
                .findMissingSkills(
                        requiredSkills,
                        matchedSkills
                );
    }
}