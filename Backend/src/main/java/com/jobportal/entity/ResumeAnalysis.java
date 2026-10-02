package com.jobportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resume_analyses")
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false)
    private double matchPercentage;

    @ElementCollection
    @CollectionTable(
            name = "resume_analysis_matched_skills",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "skill", nullable = false)
    private List<String> matchedSkills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "resume_analysis_missing_skills",
            joinColumns = @JoinColumn(name = "analysis_id")
    )
    @Column(name = "skill", nullable = false)
    private List<String> missingSkills = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime analyzedAt;

    public ResumeAnalysis() {
    }

    @PrePersist
    protected void onCreate() {
        if (analyzedAt == null) {
            analyzedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public User getCandidate() {
        return candidate;
    }

    public void setCandidate(User candidate) {
        this.candidate = candidate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
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

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }
}