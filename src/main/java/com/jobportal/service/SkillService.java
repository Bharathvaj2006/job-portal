package com.jobportal.service;

import com.jobportal.dto.SkillResponse;
import com.jobportal.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<SkillResponse> getAllSkills() {

        return skillRepository.findAll()
                .stream()
                .map(skill ->
                        new SkillResponse(
                                skill.getId(),
                                skill.getName()
                        ))
                .toList();
    }
}