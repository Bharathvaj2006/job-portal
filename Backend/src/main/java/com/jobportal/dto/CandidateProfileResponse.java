package com.jobportal.dto;

import java.util.Set;

public class CandidateProfileResponse {

    private Long id;
    private String phone;
    private String location;
    private String headline;
    private String summary;

    private Long userId;
    private String name;
    private String email;
    private String role;
    private Set<String> skills;

    public CandidateProfileResponse() {
    }

    public CandidateProfileResponse(
            Long id,
            String phone,
            String location,
            String headline,
            String summary,
            Long userId,
            String name,
            String email,
            String role,
            Set<String> skills) {

        this.id = id;
        this.phone = phone;
        this.location = location;
        this.headline = headline;
        this.summary = summary;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }
}