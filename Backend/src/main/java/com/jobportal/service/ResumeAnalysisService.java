package com.jobportal.service;

import com.jobportal.dto.ResumeAnalysisResponse;
import com.jobportal.entity.CandidateResume;
import com.jobportal.entity.Job;
import com.jobportal.entity.ResumeAnalysis;
import com.jobportal.entity.User;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.ResumeAnalysisRepository;
import com.jobportal.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;

    public ResumeAnalysisService(
            JobRepository jobRepository,
            CandidateResumeService candidateResumeService,
            ResumeTextExtractor resumeTextExtractor,
            SkillParser skillParser,
            SkillMatcher skillMatcher,
            SkillMatchCalculator skillMatchCalculator,
            SkillGapAnalyzer skillGapAnalyzer,
            UserRepository userRepository,
            ResumeAnalysisRepository resumeAnalysisRepository) {

        this.jobRepository = jobRepository;
        this.candidateResumeService = candidateResumeService;
        this.resumeTextExtractor = resumeTextExtractor;
        this.skillParser = skillParser;
        this.skillMatcher = skillMatcher;
        this.skillMatchCalculator = skillMatchCalculator;
        this.skillGapAnalyzer = skillGapAnalyzer;
        this.userRepository = userRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
    }

    @Transactional
    public ResumeAnalysisResponse analyzeResume(Long jobId, String email)
            throws IOException {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found"));

        CandidateResume resume =
                candidateResumeService.getMyResumeEntity(email);

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Candidate not found"));

        String resumeText = resumeTextExtractor.extractText(resume);

        List<String> requiredSkills =
                skillParser.parseRequiredSkills(job.getRequiredSkills());

        List<String> matchedSkills =
                skillMatcher.findMatchedSkills(resumeText, requiredSkills);

        double matchPercentage =
                skillMatchCalculator.calculateMatchPercentage(
                        requiredSkills, matchedSkills);

        List<String> missingSkills =
                skillGapAnalyzer.findMissingSkills(
                        requiredSkills, matchedSkills);

        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setCandidate(candidate);
        analysis.setJob(job);
        analysis.setMatchPercentage(matchPercentage);
        analysis.setMatchedSkills(matchedSkills);
        analysis.setMissingSkills(missingSkills);

        resumeAnalysisRepository.save(analysis);

        return new ResumeAnalysisResponse(
                job.getId(),
                job.getTitle(),
                matchPercentage,
                matchedSkills,
                missingSkills
        );
    }

    @Transactional(readOnly = true)
    public ResumeAnalysisResponse getLatestAnalysis(String email) {

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Candidate not found"));

        ResumeAnalysis analysis = resumeAnalysisRepository
                .findFirstByCandidate_IdOrderByAnalyzedAtDesc(candidate.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No resume analysis found"));

        return new ResumeAnalysisResponse(
                analysis.getJob().getId(),
                analysis.getJob().getTitle(),
                analysis.getMatchPercentage(),
                analysis.getMatchedSkills(),
                analysis.getMissingSkills()
        );
    }
}