package com.jobportal.dto;

import com.jobportal.entity.ApplicationStatus;

import java.time.LocalDateTime;

public class AdminApplicationResponse {

    private Long id;

    private Long jobId;
    private String jobTitle;

    private Long candidateId;
    private String candidateName;
    private String candidateEmail;

    private Long recruiterId;
    private String recruiterName;
    private String recruiterEmail;

    private Long resumeId;
    private String resumeFileName;

    private String coverLetter;

    private ApplicationStatus status;

    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    public AdminApplicationResponse() {
    }

    public AdminApplicationResponse(
            Long id, Long jobId, String jobTitle, Long candidateId,
            String candidateName, String candidateEmail,
            Long recruiterId,
            String recruiterName, String recruiterEmail,
            Long resumeId,
            String resumeFileName, String coverLetter,
            ApplicationStatus status,
            LocalDateTime appliedAt, LocalDateTime updatedAt) {

        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.recruiterId = recruiterId;
        this.recruiterName = recruiterName;
        this.recruiterEmail = recruiterEmail;
        this.resumeId = resumeId;
        this.resumeFileName = resumeFileName;
        this.coverLetter = coverLetter;
        this.status = status;
        this.appliedAt = appliedAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public AdminApplicationResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getJobId() {
        return jobId;
    }

    public AdminApplicationResponse setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public AdminApplicationResponse setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public AdminApplicationResponse setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
        return this;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public AdminApplicationResponse setCandidateName(String candidateName) {
        this.candidateName = candidateName;
        return this;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public AdminApplicationResponse setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
        return this;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public AdminApplicationResponse setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
        return this;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public AdminApplicationResponse setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
        return this;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public AdminApplicationResponse setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
        return this;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public AdminApplicationResponse setResumeId(Long resumeId) {
        this.resumeId = resumeId;
        return this;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public AdminApplicationResponse setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
        return this;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public AdminApplicationResponse setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
        return this;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public AdminApplicationResponse setStatus(ApplicationStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public AdminApplicationResponse setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public AdminApplicationResponse setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}