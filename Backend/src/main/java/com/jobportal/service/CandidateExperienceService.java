package com.jobportal.service;

import com.jobportal.dto.CandidateExperienceRequest;
import com.jobportal.dto.ExperienceResponse;
import com.jobportal.entity.CandidateExperience;
import com.jobportal.entity.CandidateProfile;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.CandidateExperienceRepository;
import com.jobportal.repository.CandidateProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CandidateExperienceService {

    private final CandidateExperienceRepository experienceRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    public CandidateExperienceService(
            CandidateExperienceRepository experienceRepository,
            CandidateProfileRepository candidateProfileRepository) {

        this.experienceRepository = experienceRepository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Transactional
    public CandidateExperience addExperience(
            CandidateExperienceRequest request,
            String email) {

        if (request.getEndYear() != null
                && request.getEndYear() < request.getStartYear()) {

            throw new IllegalArgumentException(
                    "End year cannot be before start year");
        }

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        CandidateExperience experience =
                new CandidateExperience(
                        request.getCompany(),
                        request.getJobTitle(),
                        request.getEmploymentType(),
                        request.getStartYear(),
                        request.getEndYear(),
                        request.getDescription(),
                        profile
                );

        return experienceRepository.save(experience);
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getMyExperience(String email) {

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        return experienceRepository
                .findAllByCandidateProfileId(profile.getId())
                .stream()
                .map(experience -> new ExperienceResponse(
                        experience.getId(),
                        experience.getCompany(),
                        experience.getJobTitle(),
                        experience.getEmploymentType(),
                        experience.getStartYear(),
                        experience.getEndYear(),
                        experience.getDescription()
                ))
                .toList();
    }

    @Transactional
    public ExperienceResponse updateExperience(
            Long experienceId,
            CandidateExperienceRequest request,
            String email) {

        if (request.getEndYear() != null
                && request.getEndYear() < request.getStartYear()) {

            throw new IllegalArgumentException(
                    "End year cannot be before start year");
        }

        CandidateExperience experience =
                experienceRepository.findById(experienceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Experience not found"));

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        if (!experience.getCandidateProfile().getId()
                .equals(profile.getId())) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to modify this experience");
        }

        experience.setCompany(request.getCompany());
        experience.setJobTitle(request.getJobTitle());
        experience.setEmploymentType(request.getEmploymentType());
        experience.setStartYear(request.getStartYear());
        experience.setEndYear(request.getEndYear());
        experience.setDescription(request.getDescription());

        CandidateExperience updatedExperience =
                experienceRepository.save(experience);

        return new ExperienceResponse(
                updatedExperience.getId(),
                updatedExperience.getCompany(),
                updatedExperience.getJobTitle(),
                updatedExperience.getEmploymentType(),
                updatedExperience.getStartYear(),
                updatedExperience.getEndYear(),
                updatedExperience.getDescription()
        );
    }

    @Transactional
    public void deleteExperience(
            Long experienceId,
            String email) {

        CandidateExperience experience =
                experienceRepository.findById(experienceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Experience not found"));

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        if (!experience.getCandidateProfile().getId()
                .equals(profile.getId())) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to delete this experience");
        }

        experienceRepository.delete(experience);
    }
}