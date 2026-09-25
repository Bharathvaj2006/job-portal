package com.jobportal.service;

import com.jobportal.dto.ResumeAnalysisResponse;
import com.jobportal.entity.CandidateResume;
import com.jobportal.entity.Job;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class ResumeAnalysisService {

    private final JobRepository jobRepository;
    private final CandidateResumeService candidateResumeService;
    private final ResumeTextExtractor resumeTextExtractor;
    private final SkillParser skillParser;
    private final SkillMatcher skillMatcher;
    private final SkillMatchCalculator skillMatchCalculator;
    private final SkillGapAnalyzer skillGapAnalyzer;

    public ResumeAnalysisService(
            JobRepository jobRepository,
            CandidateResumeService candidateResumeService,
            ResumeTextExtractor resumeTextExtractor,
            SkillParser skillParser,
            SkillMatcher skillMatcher,
            SkillMatchCalculator skillMatchCalculator,
            SkillGapAnalyzer skillGapAnalyzer) {

        this.jobRepository = jobRepository;
        this.candidateResumeService = candidateResumeService;
        this.resumeTextExtractor = resumeTextExtractor;
        this.skillParser = skillParser;
        this.skillMatcher = skillMatcher;
        this.skillMatchCalculator = skillMatchCalculator;
        this.skillGapAnalyzer = skillGapAnalyzer;
    }

    @Transactional(readOnly = true)
    public ResumeAnalysisResponse analyzeResume(
            Long jobId,
            String email) throws IOException {

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job not found"));

        CandidateResume resume =
                candidateResumeService
                        .getMyResumeEntity(email);

        String resumeText =
                resumeTextExtractor.extractText(resume);

        List<String> requiredSkills =
                skillParser.parseRequiredSkills(
                        job.getRequiredSkills());

        List<String> matchedSkills =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills);

        double matchPercentage =
                skillMatchCalculator
                        .calculateMatchPercentage(
                                requiredSkills,
                                matchedSkills);

        List<String> missingSkills =
                skillGapAnalyzer.findMissingSkills(
                        requiredSkills,
                        matchedSkills);

        return new ResumeAnalysisResponse(
                job.getId(),
                job.getTitle(),
                matchPercentage,
                matchedSkills,
                missingSkills
        );
    }
}