package com.jobportal.service;

import com.jobportal.dto.PageResponse;
import com.jobportal.dto.AdminJobResponse;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobStatus;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminJobService {

    private final JobRepository jobRepository;

    public AdminJobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public PageResponse<AdminJobResponse> getAllJobs(Pageable pageable) {

        Page<AdminJobResponse> jobResponse = jobRepository.findAll(pageable).map(this::mapToResponse);
        return new PageResponse<>(
                jobResponse.getContent(),
                jobResponse.getNumber(),
                jobResponse.getSize(),
                jobResponse.getTotalElements(),
                jobResponse.getTotalPages()
        );
    }

    public AdminJobResponse closeJob(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found"));

        job.setStatus(JobStatus.CLOSED);

        Job savedJob = jobRepository.save(job);

        return mapToResponse(savedJob);
    }

    private AdminJobResponse mapToResponse(Job job) {

        return new AdminJobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getRequiredSkills(),
                job.getLocation(),
                job.getSalary(),
                job.getEmploymentType(),

                job.getRecruiter().getId(),
                job.getRecruiter().getName(),
                job.getRecruiter().getEmail(),

                job.getStatus(),
                job.getApplicationDeadline(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}