package com.jobportal.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillMatcherTest {

    private final SkillMatcher skillMatcher =
            new SkillMatcher();

    @Test
    void shouldMatchAllRequiredSkills() {

        String resumeText =
                """
                Java Developer
                Experienced in Java, Spring Boot,
                PostgreSQL and Docker.
                """;

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql",
                        "docker"
                );

        List<String> result =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills
                );

        assertEquals(
                List.of(
                        "java",
                        "spring boot",
                        "postgresql",
                        "docker"
                ),
                result
        );
    }

    @Test
    void shouldMatchOnlyAvailableSkills() {

        String resumeText =
                """
                Java Developer
                Skills: Java, Spring Boot, PostgreSQL
                """;

        List<String> requiredSkills = List.of(
                                        "java",
                                        "spring boot",
                                        "postgresql",
                                        "docker",
                                        "aws"
                                        );

        List<String> result =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills
                );

        assertEquals(
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                ),
                result
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoSkillsMatch() {

        String resumeText =
                """
                Frontend Developer
                Skills: HTML, CSS, JavaScript
                """;

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                );

        List<String> result =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills
                );

        assertEquals(
                List.of(),
                result
        );
    }

    @Test
    void shouldMatchSkillsCaseInsensitively() {

        String resumeText =
                """
                JAVA
                SPRING BOOT
                POSTGRESQL
                """;

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                );

        List<String> result =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills
                );

        assertEquals(
                List.of(
                        "java",
                        "spring boot",
                        "postgresql"
                ),
                result
        );
    }

    @Test
    void shouldNotMatchJavaInsideJavaScript() {

        String resumeText =
                """
                Frontend Developer
                Skills: JavaScript, React, HTML, CSS
                """;

        List<String> requiredSkills =
                List.of("java");

        List<String> result =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills
                );

        assertEquals(
                List.of(),
                result
        );
    }

    @Test
    void shouldMatchMultiWordSkill() {

        String resumeText =
                """
                Backend Developer
                Worked extensively with Spring Boot
                and Spring Framework.
                """;

        List<String> requiredSkills =
                List.of(
                        "spring boot"
                );

        List<String> result =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills
                );

        assertEquals(
                List.of("spring boot"),
                result
        );
    }

    @Test
    void shouldReturnEmptyListForEmptyResume() {

        String resumeText = "";

        List<String> requiredSkills =
                List.of(
                        "java",
                        "spring boot"
                );

        List<String> result =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills
                );

        assertEquals(
                List.of(),
                result
        );
    }

    @Test
    void shouldIgnoreBlankSkills() {

        String resumeText =
                "Java Spring Boot Developer";

        List<String> requiredSkills =
                List.of(
                        "java",
                        "",
                        " ",
                        "spring boot"
                );

        List<String> result =
                skillMatcher.findMatchedSkills(
                        resumeText,
                        requiredSkills
                );

        assertEquals(
                List.of(
                        "java",
                        "spring boot"
                ),
                result
        );
    }
}