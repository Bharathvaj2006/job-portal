package com.jobportal.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SkillMatchCalculator {

    public double calculateMatchPercentage(
            List<String> requiredSkills,
            List<String> matchedSkills) {

        if (requiredSkills == null
                || requiredSkills.isEmpty()) {

            return 0.0;
        }

        if (matchedSkills == null
                || matchedSkills.isEmpty()) {

            return 0.0;
        }

        double percentage =
                ((double) matchedSkills.size()
                        / requiredSkills.size())
                        * 100;

        return BigDecimal.valueOf(percentage)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}