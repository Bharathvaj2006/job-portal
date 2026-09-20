package com.jobportal.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class CandidateSkillsRequest {

    @NotEmpty(message = "At least one skill is required")
    private Set<Long> skillIds;

    public CandidateSkillsRequest() {
    }

    public Set<Long> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(Set<Long> skillIds) {
        this.skillIds = skillIds;
    }
}