package com.jobportal.service;

import com.jobportal.dto.AdminApplicationResponse;
import com.jobportal.dto.PageResponse;
import com.jobportal.entity.ApplicationStatus;
import com.jobportal.entity.ApplicationStatusHistory;
import com.jobportal.entity.JobApplication;
import com.jobportal.entity.User;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.ApplicationStatusHistoryRepository;
import com.jobportal.repository.JobApplicationRepository;

import com.jobportal.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;
    private final ApplicationStatusHistoryRepository applicationStatusHistoryRepository;

    public AdminApplicationService(JobApplicationRepository jobApplicationRepository,
                                   UserRepository userRepository,
                                   ApplicationStatusHistoryRepository applicationStatusHistoryRepository) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
        this.applicationStatusHistoryRepository = applicationStatusHistoryRepository;
    }

    public PageResponse<AdminApplicationResponse> getAllApplications(
            Pageable pageable) {

        Page<AdminApplicationResponse> response = jobApplicationRepository.findAll(pageable)
                .map(this::mapToResponse);
        return new PageResponse<>(
                response.getContent(),
                response.getNumber(),
                response.getSize(),
                response.getTotalElements(),
                response.getTotalPages()
        );
    }

    @Transactional
    public AdminApplicationResponse updateApplicationStatus(
            Long applicationId,
            ApplicationStatus newStatus,
            String adminEmail) {

        JobApplication application =
                jobApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found"
                                ));

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid administrator"
                        ));

        ApplicationStatus oldStatus =
                application.getStatus();

        validateStatusTransition(oldStatus, newStatus);

        application.setStatus(newStatus);

        JobApplication savedApplication =
                jobApplicationRepository.save(application);

        ApplicationStatusHistory history =
                new ApplicationStatusHistory();

        history.setApplication(savedApplication);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy(admin);

        applicationStatusHistoryRepository.save(history);

        return mapToResponse(savedApplication);
    }

    private void validateStatusTransition(
            ApplicationStatus oldStatus,
            ApplicationStatus newStatus) {

        if (oldStatus == newStatus) {
            throw new IllegalArgumentException(
                    "Application is already in this status"
            );
        }

        boolean validTransition = switch (oldStatus) {

            case APPLIED ->
                    newStatus == ApplicationStatus.SHORTLISTED
                            || newStatus == ApplicationStatus.REJECTED
                            || newStatus == ApplicationStatus.WITHDRAWN;

            case SHORTLISTED ->
                    newStatus == ApplicationStatus.HIRED
                            || newStatus == ApplicationStatus.REJECTED;

            case REJECTED, HIRED, WITHDRAWN ->
                    false;
        };

        if (!validTransition) {
            throw new IllegalArgumentException(
                    "Invalid application status transition from "
                            + oldStatus
                            + " to "
                            + newStatus
            );
        }
    }

    private AdminApplicationResponse mapToResponse(
            JobApplication application) {

        return new AdminApplicationResponse(
                application.getId(),

                application.getJob().getId(),
                application.getJob().getTitle(),

                application.getCandidate().getId(),
                application.getCandidate().getName(),
                application.getCandidate().getEmail(),

                application.getJob().getRecruiter().getId(),
                application.getJob().getRecruiter().getName(),
                application.getJob().getRecruiter().getEmail(),

                application.getResume().getId(),
                application.getResume().getFileName(),

                application.getCoverLetter(),

                application.getStatus(),

                application.getAppliedAt(),
                application.getUpdatedAt()
        );
    }
}