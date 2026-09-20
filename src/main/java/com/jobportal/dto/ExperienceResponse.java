package com.jobportal.dto;

public class ExperienceResponse {

    private Long id;
    private String company;
    private String jobTitle;
    private String employmentType;
    private Integer startYear;
    private Integer endYear;
    private String description;

    public ExperienceResponse() {
    }

    public ExperienceResponse(Long id,
                              String company,
                              String jobTitle,
                              String employmentType,
                              Integer startYear,
                              Integer endYear,
                              String description) {
        this.id = id;
        this.company = company;
        this.jobTitle = jobTitle;
        this.employmentType = employmentType;
        this.startYear = startYear;
        this.endYear = endYear;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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