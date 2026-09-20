package com.jobportal.service;

import com.jobportal.dto.*;
import com.jobportal.entity.*;
import com.jobportal.exception.ResourceAlreadyExistsException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final CandidateResumeRepository candidateResumeRepository;
    private final ApplicationStatusHistoryRepository applicationStatusHistoryRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            JobRepository jobRepository,
            CandidateProfileRepository candidateProfileRepository,
            CandidateResumeRepository candidateResumeRepository,
            ApplicationStatusHistoryRepository applicationStatusHistoryRepository) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.candidateResumeRepository = candidateResumeRepository;
        this.applicationStatusHistoryRepository = applicationStatusHistoryRepository;
    }

    public JobApplicationResponse applyForJob(
            Long jobId,
            JobApplicationRequest request,
            User candidate) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id: " + jobId
                        ));

        if (job.getStatus() != JobStatus.ACTIVE) {
            throw new AccessDeniedException(
                    "Applications are closed for this job"
            );
        }

        if (!job.getApplicationDeadline()
                .isAfter(LocalDateTime.now())) {

            throw new AccessDeniedException(
                    "Application deadline has passed"
            );
        }

        if (jobApplicationRepository
                .existsByCandidateIdAndJobId(
                        candidate.getId(),
                        jobId)) {

            throw new ResourceAlreadyExistsException(
                    "You have already applied for this job"
            );
        }

        CandidateProfile profile =
                candidateProfileRepository
                        .findByUserEmail(candidate.getEmail())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"
                                ));

        CandidateResume resume =
                candidateResumeRepository
                        .findByCandidateProfileId(profile.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Please upload a resume before applying"
                                ));

        JobApplication application =
                new JobApplication();

        application.setCandidate(candidate);
        application.setJob(job);
        application.setResume(resume);
        application.setCoverLetter(request.getCoverLetter());

        JobApplication savedApplication =
                jobApplicationRepository.save(application);

        return mapToResponse(savedApplication);
    }

    public PageResponse<JobApplicationResponse> getMyApplications(
            User candidate,
            Pageable pageable) {

        Page<JobApplication> applications =
                jobApplicationRepository.findByCandidateId(
                        candidate.getId(),
                        pageable
                );

        Page<JobApplicationResponse> applicationPage =
                applications.map(this::mapToResponse);

        return new PageResponse<>(
                applicationPage.getContent(),
                applicationPage.getNumber(),
                applicationPage.getSize(),
                applicationPage.getTotalElements(),
                applicationPage.getTotalPages()
        );
    }

    public PageResponse<JobApplicationResponse> getRecruiterApplications(
            User recruiter,
            Pageable pageable) {

        Page<JobApplication> applications =
                jobApplicationRepository
                        .findByJobRecruiterId(
                                recruiter.getId(),
                                pageable
                        );

        Page<JobApplicationResponse> applicationPage =
                applications.map(this::mapToResponse);

        return new PageResponse<>(
                applicationPage.getContent(),
                applicationPage.getNumber(),
                applicationPage.getSize(),
                applicationPage.getTotalElements(),
                applicationPage.getTotalPages()
        );
    }

    public JobApplicationResponse updateApplicationStatus(
            Long applicationId,
            ApplicationStatus newStatus,
            User recruiter) {

        JobApplication application =
                jobApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found with id: "
                                                + applicationId
                                ));

        if (!application.getJob()
                .getRecruiter()
                .getId()
                .equals(recruiter.getId())) {

            throw new AccessDeniedException(
                    "You do not have permission to update this application"
            );
        }

        ApplicationStatus currentStatus =
                application.getStatus();

        boolean validTransition =
                switch (currentStatus) {

                    case APPLIED ->
                            newStatus == ApplicationStatus.SHORTLISTED
                                    || newStatus == ApplicationStatus.REJECTED;

                    case SHORTLISTED ->
                            newStatus == ApplicationStatus.HIRED
                                    || newStatus == ApplicationStatus.REJECTED;

                    case REJECTED, HIRED, WITHDRAWN ->
                            false;
                };

        if (!validTransition) {
            throw new IllegalArgumentException(
                    "Invalid status transition from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }

        ApplicationStatus oldStatus = application.getStatus();

        application.setStatus(newStatus);

        JobApplication updatedApplication = jobApplicationRepository.save(application);

        ApplicationStatusHistory history = new ApplicationStatusHistory();

        history.setApplication(updatedApplication);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy(recruiter);

        applicationStatusHistoryRepository.save(history);

        return mapToResponse(updatedApplication);
    }

    public JobApplicationResponse withdrawApplication(
            Long applicationId,
            User candidate) {

        JobApplication application =
                jobApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found with id: "
                                                + applicationId
                                ));

        if (!application.getCandidate().getId().equals(candidate.getId())) {

            throw new AccessDeniedException(
                    "You do not have permission to withdraw this application"
            );
        }

        ApplicationStatus currentStatus = application.getStatus();

        if (currentStatus != ApplicationStatus.APPLIED) {

            throw new IllegalArgumentException(
                    "Application cannot be withdrawn from status: "
                            + currentStatus
            );
        }

        ApplicationStatus oldStatus = application.getStatus();

        application.setStatus(ApplicationStatus.WITHDRAWN);

        JobApplication updatedApplication = jobApplicationRepository.save(application);

        ApplicationStatusHistory history = new ApplicationStatusHistory();

        history.setApplication(updatedApplication);
        history.setOldStatus(oldStatus);
        history.setNewStatus(ApplicationStatus.WITHDRAWN);
        history.setChangedBy(candidate);

        applicationStatusHistoryRepository.save(history);

        return mapToResponse(updatedApplication);
    }

    public PageResponse<ApplicationStatusHistoryResponse> getApplicationHistory(
            Long applicationId,
            User user,
            Pageable pageable) {

        JobApplication application =
                jobApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found with id: "
                                                + applicationId
                                ));

        boolean isCandidate =
                application.getCandidate()
                        .getId()
                        .equals(user.getId());

        boolean isRecruiter =
                application.getJob()
                        .getRecruiter()
                        .getId()
                        .equals(user.getId());

        if (!isCandidate && !isRecruiter) {
            throw new AccessDeniedException(
                    "You do not have permission to view this application history"
            );
        }

        Page<ApplicationStatusHistory> history =
                applicationStatusHistoryRepository
                        .findByApplicationId(
                                applicationId,
                                pageable
                        );

        Page<ApplicationStatusHistoryResponse> historyPage =
                history.map(this::mapHistoryToResponse);

        return new PageResponse<>(
                historyPage.getContent(),
                historyPage.getNumber(),
                historyPage.getSize(),
                historyPage.getTotalElements(),
                historyPage.getTotalPages()
        );
    }

    private JobApplicationResponse mapToResponse(
            JobApplication application) {

        JobApplicationResponse response =
                new JobApplicationResponse();

        response.setId(application.getId());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCandidateId(application.getCandidate().getId());
        response.setCandidateName(application.getCandidate().getName());
        response.setResumeId(application.getResume().getId());
        response.setResumeFileName(application.getResume().getFileName());
        response.setCoverLetter(application.getCoverLetter());
        response.setStatus(application.getStatus());
        response.setAppliedAt(application.getAppliedAt());
        response.setUpdatedAt(application.getUpdatedAt());

        return response;
    }

    private ApplicationStatusHistoryResponse mapHistoryToResponse(
            ApplicationStatusHistory history) {

        ApplicationStatusHistoryResponse response =
                new ApplicationStatusHistoryResponse();

        response.setId(history.getId());
        response.setApplicationId(
                history.getApplication().getId()
        );
        response.setOldStatus(history.getOldStatus());
        response.setNewStatus(history.getNewStatus());
        response.setChangedById(
                history.getChangedBy().getId()
        );
        response.setChangedByName(
                history.getChangedBy().getName()
        );
        response.setChangedAt(history.getChangedAt());

        return response;
    }
}