package com.jobportal.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CandidateExperienceRequest {

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @NotNull(message = "Start year is required")
    @Min(value = 1900, message = "Invalid start year")
    @Max(value = 2100, message = "Invalid start year")
    private Integer startYear;

    @Min(value = 1900, message = "Invalid end year")
    @Max(value = 2100, message = "Invalid end year")
    private Integer endYear;

    @Size(max = 2000,
            message = "Description must not exceed 2000 characters")
    private String description;

    public CandidateExperienceRequest() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}