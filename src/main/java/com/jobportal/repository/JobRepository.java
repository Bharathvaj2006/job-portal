package com.jobportal.repository;

import com.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobRepository
        extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Page<Job> findByRecruiterId(Long recruiterId, Pageable pageable);
}