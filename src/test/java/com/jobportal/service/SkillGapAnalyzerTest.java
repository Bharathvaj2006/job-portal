package com.jobportal.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillGapAnalyzerTest {

    private final SkillGapAnalyzer analyzer =
            new SkillGapAnalyzer();

    @Test
    void shouldIdentifyMissingSkills() {

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

        List<String> result =
                analyzer.findMissingSkills(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(
                List.of("aws"),
                result
        );
    }

    @Test
    void shouldReturnEmptyListWhenAllSkillsMatch() {

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

        List<String> result =
                analyzer.findMissingSkills(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(
                List.of(),
                result
        );
    }

    @Test
    void shouldReturnAllRequiredSkillsWhenNoneMatch() {

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                );

        List<String> matchedSkills =
                List.of();

        List<String> result =
                analyzer.findMissingSkills(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(
                requiredSkills,
                result
        );
    }

    @Test
    void shouldReturnEmptyListWhenRequiredSkillsAreEmpty() {

        List<String> requiredSkills =
                List.of();

        List<String> matchedSkills =
                List.of("java");

        List<String> result =
                analyzer.findMissingSkills(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(
                List.of(),
                result
        );
    }

    @Test
    void shouldReturnAllRequiredSkillsWhenMatchedSkillsAreNull() {

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot"
                );

        List<String> result =
                analyzer.findMissingSkills(
                        requiredSkills,
                        null
                );

        assertEquals(
                requiredSkills,
                result
        );
    }

    @Test
    void shouldPreserveRequiredSkillOrder() {

        List<String> requiredSkills =
                List.of(
                        "java",
                        "aws",
                        "docker",
                        "kubernetes"
                );

        List<String> matchedSkills =
                List.of(
                        "java",
                        "docker"
                );

        List<String> result =
                analyzer.findMissingSkills(
                        requiredSkills,
                        matchedSkills
                );

        assertEquals(
                List.of(
                        "aws",
                        "kubernetes"
                ),
                result
        );
    }
}