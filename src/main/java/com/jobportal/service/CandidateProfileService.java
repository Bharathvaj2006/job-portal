package com.jobportal.service;

import com.jobportal.dto.CandidateProfileRequest;
import com.jobportal.dto.CandidateProfileResponse;
import com.jobportal.entity.CandidateProfile;
import com.jobportal.entity.Skill;
import com.jobportal.entity.User;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.CandidateProfileRepository;
import com.jobportal.repository.SkillRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.jobportal.exception.ResourceAlreadyExistsException;
import com.jobportal.dto.CandidateSkillsRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public CandidateProfileService(
            CandidateProfileRepository candidateProfileRepository,
            UserRepository userRepository,
            SkillRepository skillRepository) {

        this.candidateProfileRepository = candidateProfileRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    public CandidateProfile createProfile(
            CandidateProfileRequest request,
            String email) {

        if (candidateProfileRepository.existsByUserEmail(email)) {
            throw new ResourceAlreadyExistsException(
                    "Candidate profile already exists");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        CandidateProfile profile = new CandidateProfile(
                request.getPhone(),
                request.getLocation(),
                request.getHeadline(),
                request.getSummary(),
                user
        );

        return candidateProfileRepository.save(profile);
    }

    public CandidateProfileResponse getMyProfile(String email) {

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        return toResponse(profile);
    }

    public CandidateProfileResponse updateMyProfile(
            CandidateProfileRequest request,
            String email) {

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        profile.setPhone(request.getPhone());
        profile.setLocation(request.getLocation());
        profile.setHeadline(request.getHeadline());
        profile.setSummary(request.getSummary());

        CandidateProfile updatedProfile =
                candidateProfileRepository.save(profile);

        return toResponse(updatedProfile);
    }

    public void deleteProfile(String email) {
        CandidateProfile profile=candidateProfileRepository.findByUserEmail(email).
                orElseThrow(() ->
                        new ResourceNotFoundException("Candidate profile not found"));
        candidateProfileRepository.delete(profile);
    }

    @Transactional
    public CandidateProfileResponse updateSkills(
            CandidateSkillsRequest request,
            String email) {

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        Set<Long> skillIds = request.getSkillIds();

        List<Skill> skills =
                skillRepository.findAllByIdIn(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new IllegalArgumentException(
                    "One or more skill IDs are invalid");
        }

        profile.getSkills().clear();
        profile.getSkills().addAll(skills);

        return toResponse(profile);
    }

    private CandidateProfileResponse toResponse(
            CandidateProfile profile) {

        User user = profile.getUser();

        Set<String> skills = profile.getSkills()
                .stream()
                .map(Skill::getName)
                .collect(Collectors.toSet());

        return new CandidateProfileResponse(
                profile.getId(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getHeadline(),
                profile.getSummary(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                skills
        );
    }
}