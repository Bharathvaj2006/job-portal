package com.jobportal.dto;

public class EducationResponse {

    private Long id;
    private String degree;
    private String fieldOfStudy;
    private String institution;
    private Integer startYear;
    private Integer endYear;

    public EducationResponse() {
    }

    public EducationResponse(Long id,
                             String degree,
                             String fieldOfStudy,
                             String institution,
                             Integer startYear,
                             Integer endYear) {
        this.id = id;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.institution = institution;
        this.startYear = startYear;
        this.endYear = endYear;
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
}