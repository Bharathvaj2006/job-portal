package com.jobportal.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "job_applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_candidate_job",
                        columnNames = {"candidate_id", "job_id"}
                )
        }
)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "candidate_id",
            nullable = false
    )
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "job_id",
            nullable = false
    )
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "resume_id",
            nullable = false
    )
    private CandidateResume resume;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public JobApplication() {
    }

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.appliedAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = ApplicationStatus.APPLIED;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public JobApplication setId(Long id) {
        this.id = id;
        return this;
    }

    public User getCandidate() {
        return candidate;
    }

    public JobApplication setCandidate(User candidate) {
        this.candidate = candidate;
        return this;
    }

    public Job getJob() {
        return job;
    }

    public JobApplication setJob(Job job) {
        this.job = job;
        return this;
    }

    public CandidateResume getResume() {
        return resume;
    }

    public JobApplication setResume(CandidateResume resume) {
        this.resume = resume;
        return this;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public JobApplication setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
        return this;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public JobApplication setStatus(ApplicationStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}