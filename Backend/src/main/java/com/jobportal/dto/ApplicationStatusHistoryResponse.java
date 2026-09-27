package com.jobportal.dto;

import com.jobportal.entity.ApplicationStatus;

import java.time.LocalDateTime;

public class ApplicationStatusHistoryResponse {

    private Long id;
    private Long applicationId;
    private ApplicationStatus oldStatus;
    private ApplicationStatus newStatus;
    private Long changedById;
    private String changedByName;
    private LocalDateTime changedAt;

    public ApplicationStatusHistoryResponse() {
    }

    public Long getId() {
        return id;
    }

    public ApplicationStatusHistoryResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public ApplicationStatusHistoryResponse setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public ApplicationStatus getOldStatus() {
        return oldStatus;
    }

    public ApplicationStatusHistoryResponse setOldStatus(ApplicationStatus oldStatus) {
        this.oldStatus = oldStatus;
        return this;
    }

    public Long getChangedById() {
        return changedById;
    }

    public ApplicationStatusHistoryResponse setChangedById(Long changedById) {
        this.changedById = changedById;
        return this;
    }

    public ApplicationStatus getNewStatus() {
        return newStatus;
    }

    public ApplicationStatusHistoryResponse setNewStatus(ApplicationStatus newStatus) {
        this.newStatus = newStatus;
        return this;
    }

    public String getChangedByName() {
        return changedByName;
    }

    public ApplicationStatusHistoryResponse setChangedByName(String changedByName) {
        this.changedByName = changedByName;
        return this;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public ApplicationStatusHistoryResponse setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
        return this;
    }
}