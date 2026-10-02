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
import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.repository.ResumeAnalysisRepository;
import com.jobportal.entity.ResumeAnalysis;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeAnalysisServiceIntegrationTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CandidateResumeService candidateResumeService;

    @Mock
    private ResumeTextExtractor resumeTextExtractor;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResumeAnalysisRepository resumeAnalysisRepository;

    private final SkillParser skillParser = new SkillParser();
    private final SkillMatcher skillMatcher = new SkillMatcher();
    private final SkillMatchCalculator skillMatchCalculator =
            new SkillMatchCalculator();
    private final SkillGapAnalyzer skillGapAnalyzer =
            new SkillGapAnalyzer();

    @Test
    void shouldPerformCompleteResumeAnalysis() throws Exception {

        Long jobId = 15L;
        String email = "candidate@example.com";

        Job job = mock(Job.class);

        when(job.getId()).thenReturn(jobId);

        User candidate = mock(User.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(candidate));

        when(job.getTitle())
                .thenReturn("Java Backend Developer");

        when(job.getRequiredSkills())
                .thenReturn(
                        "Java, Spring Boot, PostgreSQL, Docker, AWS"
                );

        CandidateResume resume = mock(CandidateResume.class);

        String resumeText = """
                Experienced Java developer with strong knowledge of
                Spring Boot and PostgreSQL.
                Built REST APIs using Java and Spring Boot.
                """;

        when(jobRepository.findById(jobId))
                .thenReturn(Optional.of(job));

        when(candidateResumeService.getMyResumeEntity(email))
                .thenReturn(resume);

        when(resumeTextExtractor.extractText(resume))
                .thenReturn(resumeText);

        ResumeAnalysisService service =
                new ResumeAnalysisService(
                        jobRepository,
                        candidateResumeService,
                        resumeTextExtractor,
                        skillParser,
                        skillMatcher,
                        skillMatchCalculator,
                        skillGapAnalyzer,
                        userRepository,
                        resumeAnalysisRepository
                );

        ResumeAnalysisResponse response =
                service.analyzeResume(jobId, email);

        assertNotNull(response);

        assertEquals(
                jobId,
                response.getJobId()
        );

        assertEquals(
                "Java Backend Developer",
                response.getJobTitle()
        );

        assertEquals(
                60.0,
                response.getMatchPercentage()
        );

        assertEquals(
                java.util.List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                ),
                response.getMatchedSkills()
        );

        assertEquals(
                java.util.List.of(
                        "docker",
                        "aws"
                ),
                response.getMissingSkills()
        );

        verify(jobRepository)
                .findById(jobId);

        verify(candidateResumeService)
                .getMyResumeEntity(email);

        verify(resumeTextExtractor)
                .extractText(resume);

        verify(userRepository).findByEmail(email);

        verify(resumeAnalysisRepository)
                .save(any(ResumeAnalysis.class));
    }
}