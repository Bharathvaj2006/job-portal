package com.jobportal.service;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillParserTest {

    private final SkillParser skillParser =
            new SkillParser();

    @Test
    void shouldParseRequiredSkills() {

        String requiredSkills =
                "Java, Spring Boot, PostgreSQL, React, Docker";

        List<String> result =
                skillParser.parseRequiredSkills(
                        requiredSkills);

        assertEquals(
                List.of(
                        "java",
                        "spring boot",
                        "postgresql",
                        "react",
                        "docker"
                ),
                result
        );
    }

    @Test
    void shouldTrimSpacesAndNormalizeCase() {

        String requiredSkills =
                " Java , SPRING BOOT , PostgreSQL ";

        List<String> result =
                skillParser.parseRequiredSkills(
                        requiredSkills);

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
    void shouldRemoveDuplicateSkills() {

        String requiredSkills =
                "Java, Spring Boot, Java, Docker, java";

        List<String> result =
                skillParser.parseRequiredSkills(
                        requiredSkills);

        assertEquals(
                List.of(
                        "java",
                        "spring boot",
                        "docker"
                ),
                result
        );
    }

    @Test
    void shouldIgnoreBlankSkills() {

        String requiredSkills =
                "Java, , Spring Boot, , Docker,";

        List<String> result =
                skillParser.parseRequiredSkills(
                        requiredSkills);

        assertEquals(
                List.of(
                        "java",
                        "spring boot",
                        "docker"
                ),
                result
        );
    }

    @Test
    void shouldReturnEmptyListForNull() {

        List<String> result =
                skillParser.parseRequiredSkills(null);

        assertEquals(
                List.of(),
                result
        );
    }

    @Test
    void shouldReturnEmptyListForBlankString() {

        List<String> result =
                skillParser.parseRequiredSkills("   ");

        assertEquals(
                List.of(),
                result
        );
    }
}