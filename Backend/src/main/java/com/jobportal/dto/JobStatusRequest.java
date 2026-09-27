package com.jobportal.dto;

import com.jobportal.entity.JobStatus;
import jakarta.validation.constraints.NotNull;

public class JobStatusRequest {

    @NotNull(message = "Job status is required")
    private JobStatus status;

    public JobStatusRequest() {
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }
}