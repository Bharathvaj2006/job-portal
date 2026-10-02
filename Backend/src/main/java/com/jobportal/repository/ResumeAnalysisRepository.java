package com.jobportal.repository;

import com.jobportal.entity.ResumeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeAnalysisRepository
        extends JpaRepository<ResumeAnalysis, Long> {

    Optional<ResumeAnalysis> findFirstByCandidate_IdOrderByAnalyzedAtDesc(Long candidateId);
}