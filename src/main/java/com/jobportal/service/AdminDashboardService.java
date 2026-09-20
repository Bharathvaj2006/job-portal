package com.jobportal.service;

import com.jobportal.dto.AdminDashboardResponse;
import com.jobportal.entity.ApplicationStatus;
import com.jobportal.entity.JobStatus;
import com.jobportal.entity.Role;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public AdminDashboardService(
            UserRepository userRepository,
            JobRepository jobRepository,
            JobApplicationRepository jobApplicationRepository) {

        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public AdminDashboardResponse getDashboardStatistics() {

        long totalUsers = userRepository.count();

        long totalCandidates =
                userRepository.countByRole(Role.CANDIDATE);

        long totalRecruiters =
                userRepository.countByRole(Role.RECRUITER);

        long totalAdmins =
                userRepository.countByRole(Role.ADMIN);

        long totalJobs = jobRepository.count();

        long activeJobs =
                jobRepository.countByStatus(JobStatus.ACTIVE);

        long closedJobs =
                jobRepository.countByStatus(JobStatus.CLOSED);

        long totalApplications =
                jobApplicationRepository.count();

        long appliedApplications =
                jobApplicationRepository.countByStatus(
                        ApplicationStatus.APPLIED
                );

        long shortlistedApplications =
                jobApplicationRepository.countByStatus(
                        ApplicationStatus.SHORTLISTED
                );

        long rejectedApplications =
                jobApplicationRepository.countByStatus(
                        ApplicationStatus.REJECTED
                );

        long hiredApplications =
                jobApplicationRepository.countByStatus(
                        ApplicationStatus.HIRED
                );

        long withdrawnApplications =
                jobApplicationRepository.countByStatus(
                        ApplicationStatus.WITHDRAWN
                );

        return new AdminDashboardResponse(
                totalUsers,
                totalCandidates,
                totalRecruiters,
                totalAdmins,

                totalJobs,
                activeJobs,
                closedJobs,

                totalApplications,
                appliedApplications,
                shortlistedApplications,
                rejectedApplications,
                hiredApplications,
                withdrawnApplications
        );
    }
}