package com.jobportal.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillMatchCalculatorTest {

    private final SkillMatchCalculator calculator =
            new SkillMatchCalculator();

    @Test
    void shouldCalculateFullMatch() {

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                );

        List<String> matchedSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                );

        double result =
                calculator.calculateMatchPercentage(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(100.0, result);
    }

    @Test
    void shouldCalculatePartialMatch() {

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql",
                        "docker",
                        "aws"
                );

        List<String> matchedSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql",
                        "docker"
                );

        double result =
                calculator.calculateMatchPercentage(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(80.0, result);
    }

    @Test
    void shouldRoundPercentageToTwoDecimalPlaces() {

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                );

        List<String> matchedSkills =
                List.of(
                        "java",
                        "spring boot"
                );

        double result =
                calculator.calculateMatchPercentage(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(66.67, result);
    }

    @Test
    void shouldReturnZeroWhenNoSkillsMatch() {

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                );

        List<String> matchedSkills =
                List.of();

        double result =
                calculator.calculateMatchPercentage(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(0.0, result);
    }

    @Test
    void shouldReturnZeroWhenRequiredSkillsAreEmpty() {

        List<String> requiredSkills =
                List.of();

        List<String> matchedSkills =
                List.of("java");

        double result =
                calculator.calculateMatchPercentage(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(0.0, result);
    }

    @Test
    void shouldReturnZeroWhenRequiredSkillsAreNull() {

        List<String> matchedSkills =
                List.of("java");

        double result =
                calculator.calculateMatchPercentage(
                        null,
                        matchedSkills
                );

        assertEquals(0.0, result);
    }

    @Test
    void shouldReturnZeroWhenMatchedSkillsAreNull() {

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot"
                );

        double result =
                calculator.calculateMatchPercentage(
                        requiredSkills,
                        null
                );

        assertEquals(0.0, result);
    }
}