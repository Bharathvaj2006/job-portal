package com.jobportal.dto;

import java.util.List;

public class ResumeAnalysisResponse {

    private Long jobId;

    private String jobTitle;

    private double matchPercentage;

    private List<String> matchedSkills;

    private List<String> missingSkills;

    public ResumeAnalysisResponse() {
    }

    public ResumeAnalysisResponse(
            Long jobId,
            String jobTitle,
            double matchPercentage,
            List<String> matchedSkills,
            List<String> missingSkills) {

        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.matchPercentage = matchPercentage;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(
            List<String> matchedSkills) {

        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(
            List<String> missingSkills) {

        this.missingSkills = missingSkills;
    }
}