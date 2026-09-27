package com.jobportal.service;

import com.jobportal.dto.CandidateEducationRequest;
import com.jobportal.dto.EducationResponse;
import com.jobportal.entity.CandidateEducation;
import com.jobportal.entity.CandidateProfile;
import com.jobportal.repository.CandidateEducationRepository;
import com.jobportal.repository.CandidateProfileRepository;
import com.jobportal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CandidateEducationService {

    private final CandidateEducationRepository educationRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    public CandidateEducationService(
            CandidateEducationRepository educationRepository,
            CandidateProfileRepository candidateProfileRepository) {

        this.educationRepository = educationRepository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Transactional
    public CandidateEducation addEducation(
            CandidateEducationRequest request,
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

        CandidateEducation education =
                new CandidateEducation(
                        request.getDegree(),
                        request.getFieldOfStudy(),
                        request.getInstitution(),
                        request.getStartYear(),
                        request.getEndYear(),
                        profile
                );

        return educationRepository.save(education);
    }

    @Transactional(readOnly = true)
    public List<EducationResponse> getMyEducation(String email) {

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        return educationRepository
                .findAllByCandidateProfileId(profile.getId())
                .stream()
                .map(education -> new EducationResponse(
                        education.getId(),
                        education.getDegree(),
                        education.getFieldOfStudy(),
                        education.getInstitution(),
                        education.getStartYear(),
                        education.getEndYear()
                ))
                .toList();
    }

    @Transactional
    public EducationResponse updateEducation(
            Long educationId,
            CandidateEducationRequest request,
            String email) {

        if (request.getEndYear() != null
                && request.getEndYear() < request.getStartYear()) {
            throw new IllegalArgumentException(
                    "End year cannot be before start year");
        }

        CandidateEducation education =
                educationRepository.findById(educationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Education not found"));

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        if (!education.getCandidateProfile().getId()
                .equals(profile.getId())) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to modify this education");
        }

        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setInstitution(request.getInstitution());
        education.setStartYear(request.getStartYear());
        education.setEndYear(request.getEndYear());

        CandidateEducation updatedEducation =
                educationRepository.save(education);

        return new EducationResponse(
                updatedEducation.getId(),
                updatedEducation.getDegree(),
                updatedEducation.getFieldOfStudy(),
                updatedEducation.getInstitution(),
                updatedEducation.getStartYear(),
                updatedEducation.getEndYear()
        );
    }

    @Transactional
    public void deleteEducation(Long educationId, String email) {

        CandidateEducation education =
                educationRepository.findById(educationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Education not found"));

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        if (!education.getCandidateProfile().getId()
                .equals(profile.getId())) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to delete this education");
        }

        educationRepository.delete(education);
    }
}