package com.jobportal.repository;

import com.jobportal.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    boolean existsByCandidateIdAndJobId(
            Long candidateId,
            Long jobId
    );

    Optional<JobApplication> findByCandidateIdAndJobId(
            Long candidateId,
            Long jobId
    );

    Page<JobApplication> findByCandidateId(
            Long candidateId,
            Pageable pageable
    );

    Page<JobApplication> findByJobRecruiterId(
            Long recruiterId,
            Pageable pageable
    );
}