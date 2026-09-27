package com.jobportal.repository;

import com.jobportal.entity.CandidateResume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateResumeRepository
        extends JpaRepository<CandidateResume, Long> {

    Optional<CandidateResume> findByCandidateProfileId(
            Long candidateProfileId);

    boolean existsByCandidateProfileId(Long candidateProfileId);
}