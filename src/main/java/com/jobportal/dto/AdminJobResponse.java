package com.jobportal.dto;

import com.jobportal.entity.EmploymentType;
import com.jobportal.entity.JobStatus;

import java.time.LocalDateTime;

public class AdminJobResponse {

    private Long id;
    private String title;
    private String description;
    private String requiredSkills;
    private String location;
    private Double salary;
    private EmploymentType employmentType;

    private Long recruiterId;
    private String recruiterName;
    private String recruiterEmail;

    private JobStatus status;
    private LocalDateTime applicationDeadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AdminJobResponse() {
    }

    public AdminJobResponse(
            Long id,
            String title,
            String description,
            String requiredSkills,
            String location,
            Double salary,
            EmploymentType employmentType,
            Long recruiterId,
            String recruiterName,
            String recruiterEmail,
            JobStatus status,
            LocalDateTime applicationDeadline,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.location = location;
        this.salary = salary;
        this.employmentType = employmentType;
        this.recruiterId = recruiterId;
        this.recruiterName = recruiterName;
        this.recruiterEmail = recruiterEmail;
        this.status = status;
        this.applicationDeadline = applicationDeadline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public AdminJobResponse setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
        return this;
    }

    public Long getId() {
        return id;
    }

    public AdminJobResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AdminJobResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AdminJobResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public AdminJobResponse setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public AdminJobResponse setLocation(String location) {
        this.location = location;
        return this;
    }

    public Double getSalary() {
        return salary;
    }

    public AdminJobResponse setSalary(Double salary) {
        this.salary = salary;
        return this;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public AdminJobResponse setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
        return this;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public AdminJobResponse setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
        return this;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public AdminJobResponse setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
        return this;
    }

    public JobStatus getStatus() {
        return status;
    }

    public AdminJobResponse setStatus(JobStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getApplicationDeadline() {
        return applicationDeadline;
    }

    public AdminJobResponse setApplicationDeadline(LocalDateTime applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AdminJobResponse setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public AdminJobResponse setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}