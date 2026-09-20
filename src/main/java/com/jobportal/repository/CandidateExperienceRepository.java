package com.jobportal.repository;

import com.jobportal.entity.CandidateExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateExperienceRepository
        extends JpaRepository<CandidateExperience, Long> {

    List<CandidateExperience> findAllByCandidateProfileId(
            Long candidateProfileId);
}