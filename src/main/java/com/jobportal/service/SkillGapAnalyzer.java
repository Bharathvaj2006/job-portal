package com.jobportal.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillGapAnalyzer {

    public List<String> findMissingSkills(
            List<String> requiredSkills,
            List<String> matchedSkills) {

        if (requiredSkills == null
                || requiredSkills.isEmpty()) {

            return List.of();
        }

        if (matchedSkills == null
                || matchedSkills.isEmpty()) {

            return new ArrayList<>(requiredSkills);
        }

        List<String> missingSkills =
                new ArrayList<>();

        for (String requiredSkill : requiredSkills) {

            if (!matchedSkills.contains(requiredSkill)) {
                missingSkills.add(requiredSkill);
            }
        }

        return missingSkills;
    }
}