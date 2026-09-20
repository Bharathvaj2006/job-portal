package com.jobportal.repository;

import com.jobportal.entity.CandidateEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateEducationRepository
        extends JpaRepository<CandidateEducation, Long> {

    List<CandidateEducation> findAllByCandidateProfileId(
            Long candidateProfileId);
}