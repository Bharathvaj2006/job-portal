package com.jobportal.service;

import com.jobportal.entity.Job;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeAnalysisServiceErrorTest {

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
    void shouldThrowExceptionWhenJobDoesNotExist() {

        Long jobId = 999L;
        String email = "candidate@example.com";

        when(jobRepository.findById(jobId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> resumeAnalysisService.analyzeResume(
                        jobId,
                        email
                )
        );

        verify(jobRepository).findById(jobId);

        verifyNoInteractions(
                candidateResumeService,
                resumeTextExtractor,
                skillParser,
                skillMatcher,
                skillMatchCalculator,
                skillGapAnalyzer
        );
    }

    @Test
    void shouldThrowExceptionWhenCandidateHasNoResume() {

        Long jobId = 15L;
        String email = "candidate@example.com";

        Job job = mock(Job.class);

        when(jobRepository.findById(jobId))
                .thenReturn(Optional.of(job));

        when(candidateResumeService.getMyResumeEntity(email))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Resume not found"
                        )
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> resumeAnalysisService.analyzeResume(
                        jobId,
                        email
                )
        );

        verify(jobRepository).findById(jobId);

        verify(candidateResumeService)
                .getMyResumeEntity(email);

        verifyNoInteractions(
                resumeTextExtractor,
                skillParser,
                skillMatcher,
                skillMatchCalculator,
                skillGapAnalyzer
        );
    }
}