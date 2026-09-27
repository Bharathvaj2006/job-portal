package com.jobportal.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SkillParser {

    public List<String> parseRequiredSkills(
            String requiredSkills) {

        if (requiredSkills == null
                || requiredSkills.isBlank()) {

            return List.of();
        }

        return Arrays.stream(
                        requiredSkills.split(",")
                )
                .map(String::trim)
                .filter(skill -> !skill.isBlank())
                .map(String::toLowerCase)
                .distinct()
                .toList();
    }
}