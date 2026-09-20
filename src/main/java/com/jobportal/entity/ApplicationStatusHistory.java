package com.jobportal.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "application_status_history")
public class ApplicationStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by", nullable = false)
    private User changedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime changedAt;

    public ApplicationStatusHistory() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.changedAt == null) {
            this.changedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public ApplicationStatusHistory setId(Long id) {
        this.id = id;
        return this;
    }

    public JobApplication getApplication() {
        return application;
    }

    public ApplicationStatusHistory setApplication(JobApplication application) {
        this.application = application;
        return this;
    }

    public ApplicationStatus getOldStatus() {
        return oldStatus;
    }

    public ApplicationStatusHistory setOldStatus(ApplicationStatus oldStatus) {
        this.oldStatus = oldStatus;
        return this;
    }

    public ApplicationStatus getNewStatus() {
        return newStatus;
    }

    public ApplicationStatusHistory setNewStatus(ApplicationStatus newStatus) {
        this.newStatus = newStatus;
        return this;
    }

    public User getChangedBy() {
        return changedBy;
    }

    public ApplicationStatusHistory setChangedBy(User changedBy) {
        this.changedBy = changedBy;
        return this;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }
}
