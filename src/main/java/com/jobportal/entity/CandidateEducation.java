package com.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidate_education")
public class CandidateEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String degree;

    @Column(nullable = false)
    private String fieldOfStudy;

    @Column(nullable = false)
    private String institution;

    @Column(nullable = false)
    private Integer startYear;

    private Integer endYear;

    @ManyToOne
    @JoinColumn(
            name = "candidate_profile_id",
            nullable = false
    )
    private CandidateProfile candidateProfile;

    public CandidateEducation() {
    }

    public CandidateEducation(
            String degree,
            String fieldOfStudy,
            String institution,
            Integer startYear,
            Integer endYear,
            CandidateProfile candidateProfile) {

        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.institution = institution;
        this.startYear = startYear;
        this.endYear = endYear;
        this.candidateProfile = candidateProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
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

    public CandidateProfile getCandidateProfile() {
        return candidateProfile;
    }

    public void setCandidateProfile(
            CandidateProfile candidateProfile) {

        this.candidateProfile = candidateProfile;
    }
}