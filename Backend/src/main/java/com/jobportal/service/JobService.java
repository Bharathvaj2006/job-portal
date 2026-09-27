package com.jobportal.service;

import com.jobportal.dto.JobRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.dto.PageResponse;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobStatus;
import com.jobportal.entity.User;
import com.jobportal.repository.JobRepository;
import org.springframework.stereotype.Service;
import com.jobportal.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import com.jobportal.entity.EmploymentType;
import com.jobportal.specification.JobSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class JobService {

    private final JobRepository jobRepository;


    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobResponse createJob(JobRequest request, User recruiter) {

        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setEmploymentType(request.getEmploymentType());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setRecruiter(recruiter);

        Job savedJob = jobRepository.save(job);

        return mapToResponse(savedJob);
    }

    public PageResponse<JobResponse> getAllJobs(
            String title,
            String location,
            EmploymentType employmentType,
            JobStatus status,
            Double minSalary,
            Pageable pageable) {

        Specification<Job> specification;

        if (status == JobStatus.ACTIVE) {

            specification = JobSpecification
                    .hasStatus(JobStatus.ACTIVE)
                    .and(JobSpecification.hasFutureDeadline());

        } else if (status == JobStatus.CLOSED) {

            specification = JobSpecification.hasStatus(JobStatus.CLOSED);

        } else {

            specification = JobSpecification
                    .hasStatus(JobStatus.ACTIVE)
                    .and(JobSpecification.hasFutureDeadline());
        }

        if (title != null && !title.isBlank()) {
            specification = specification.and(
                    JobSpecification.hasTitle(title)
            );
        }

        if (location != null && !location.isBlank()) {

            Specification<Job> locationSpecification =
                    JobSpecification.hasLocation(location);

            specification = specification == null
                    ? locationSpecification
                    : specification.and(locationSpecification);
        }

        if (employmentType != null) {

            Specification<Job> employmentTypeSpecification =
                    JobSpecification.hasEmploymentType(employmentType);

            specification = specification == null
                    ? employmentTypeSpecification
                    : specification.and(employmentTypeSpecification);
        }

        if (status != null) {

            Specification<Job> statusSpecification =
                    JobSpecification.hasStatus(status);

            specification = specification == null
                    ? statusSpecification
                    : specification.and(statusSpecification);
        }

        if (minSalary != null) {

            Specification<Job> salarySpecification =
                    JobSpecification.hasMinimumSalary(minSalary);

            specification = specification == null
                    ? salarySpecification
                    : specification.and(salarySpecification);
        }

        Page<Job> jobs;

        if (specification == null) {
            jobs = jobRepository.findAll(pageable);
        } else {
            jobs = jobRepository.findAll(specification, pageable);
        }

        Page<JobResponse> jobPage = jobs.map(this::mapToResponse);

        return new PageResponse<>(
                jobPage.getContent(),
                jobPage.getNumber(),
                jobPage.getSize(),
                jobPage.getTotalElements(),
                jobPage.getTotalPages()
        );
    }

    public JobResponse getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found with id: " + id));

        return mapToResponse(job);
    }

    public JobResponse updateJob(
            Long jobId,
            JobRequest request,
            User recruiter) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id: " + jobId
                        ));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException(
                    "You do not have permission to update this job"
            );
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setEmploymentType(request.getEmploymentType());
        job.setApplicationDeadline(request.getApplicationDeadline());

        Job updatedJob = jobRepository.save(job);

        return mapToResponse(updatedJob);
    }

    public void deleteJob(Long jobId, User recruiter) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id: " + jobId
                        ));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException(
                    "You do not have permission to delete this job"
            );
        }

        jobRepository.delete(job);
    }

    public JobResponse updateJobStatus(
            Long jobId,
            JobStatus status,
            User recruiter) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id: " + jobId
                        ));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException(
                    "You do not have permission to update this job"
            );
        }

        job.setStatus(status);

        Job updatedJob = jobRepository.save(job);

        return mapToResponse(updatedJob);
    }

    public PageResponse<JobResponse> getRecruiterJobs(
            User recruiter,
            Pageable pageable) {

        Page<Job> jobs =
                jobRepository.findByRecruiterId(
                        recruiter.getId(),
                        pageable
                );

        Page<JobResponse> jobPage =
                jobs.map(this::mapToResponse);

        return new PageResponse<>(
                jobPage.getContent(),
                jobPage.getNumber(),
                jobPage.getSize(),
                jobPage.getTotalElements(),
                jobPage.getTotalPages()
        );
    }

    private JobResponse mapToResponse(Job job) {

        JobResponse response = new JobResponse();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setRequiredSkills(job.getRequiredSkills());
        response.setLocation(job.getLocation());
        response.setSalary(job.getSalary());
        response.setEmploymentType(job.getEmploymentType());
        response.setStatus(job.getStatus());

        response.setRecruiterId(job.getRecruiter().getId());
        response.setApplicationDeadline(job.getApplicationDeadline());
        response.setRecruiterName(job.getRecruiter().getName());

        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());

        return response;
    }
}