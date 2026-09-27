package com.jobportal.dto;

import jakarta.validation.constraints.Size;

public class JobApplicationRequest {

    @Size(
            max = 5000,
            message = "Cover letter cannot exceed 5000 characters"
    )
    private String coverLetter;

    public JobApplicationRequest() {
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
}