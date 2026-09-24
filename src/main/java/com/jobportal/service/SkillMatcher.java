package com.jobportal.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SkillMatcher {

    public List<String> findMatchedSkills(
            String resumeText,
            List<String> requiredSkills) {

        if (resumeText == null
                || resumeText.isBlank()
                || requiredSkills == null
                || requiredSkills.isEmpty()) {

            return List.of();
        }

        String normalizedResume =
                resumeText.toLowerCase();

        List<String> matchedSkills =
                new ArrayList<>();

        for (String skill : requiredSkills) {

            if (skill == null || skill.isBlank()) {
                continue;
            }

            String normalizedSkill =
                    skill.trim().toLowerCase();

            String regex =
                    "(?<![a-zA-Z0-9])"
                            + Pattern.quote(normalizedSkill)
                            + "(?![a-zA-Z0-9])";

            if (Pattern.compile(regex)
                    .matcher(normalizedResume)
                    .find()) {

                matchedSkills.add(normalizedSkill);
            }
        }

        return matchedSkills;
    }
}