package com.jobportal.service;

import com.jobportal.dto.ResumeResponse;
import com.jobportal.entity.CandidateProfile;
import com.jobportal.entity.CandidateResume;
import com.jobportal.exception.ResourceAlreadyExistsException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.CandidateProfileRepository;
import com.jobportal.repository.CandidateResumeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CandidateResumeService {

    private final CandidateResumeRepository resumeRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public CandidateResumeService(
            CandidateResumeRepository resumeRepository,
            CandidateProfileRepository candidateProfileRepository) {

        this.resumeRepository = resumeRepository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Transactional
    public ResumeResponse uploadResume(
            MultipartFile file,
            String email) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Resume file cannot be empty");
        }

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        if (resumeRepository.existsByCandidateProfileId(
                profile.getId())) {

            throw new ResourceAlreadyExistsException(
                    "Resume already exists");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null
                || originalFileName.isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid resume file name");
        }

        String contentType = file.getContentType();

        if (!"application/pdf".equals(contentType)
                && !"application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                .equals(contentType)) {

            throw new IllegalArgumentException(
                    "Only PDF and DOCX files are allowed");
        }

        Path uploadPath = Paths.get(uploadDir);

        Files.createDirectories(uploadPath);

        String extension = originalFileName
                .substring(originalFileName.lastIndexOf("."));

        String storedFileName =
                UUID.randomUUID() + extension;

        Path filePath =
                uploadPath.resolve(storedFileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        CandidateResume resume =
                new CandidateResume(
                        originalFileName,
                        contentType,
                        file.getSize(),
                        filePath.toString(),
                        LocalDateTime.now(),
                        profile
                );

        CandidateResume savedResume =
                resumeRepository.save(resume);

        return toResponse(savedResume);
    }

    @Transactional(readOnly = true)
    public ResumeResponse getMyResume(String email) {

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        CandidateResume resume =
                resumeRepository.findByCandidateProfileId(
                                profile.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resume not found"));

        return toResponse(resume);
    }


    @Transactional(readOnly = true)
    public CandidateResume getMyResumeEntity(String email) {

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        return resumeRepository
                .findByCandidateProfileId(profile.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resume not found"));
    }

    @Transactional
    public ResumeResponse replaceResume(
            MultipartFile file,
            String email) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Resume file cannot be empty");
        }

        CandidateProfile profile =
                candidateProfileRepository.findByUserEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"));

        CandidateResume resume =
                resumeRepository.findByCandidateProfileId(
                                profile.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resume not found"));

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null
                || originalFileName.isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid resume file name");
        }

        String contentType = file.getContentType();

        if (!"application/pdf".equals(contentType)
                && !"application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                .equals(contentType)) {

            throw new IllegalArgumentException(
                    "Only PDF and DOCX files are allowed");
        }

        Path uploadPath = Paths.get(uploadDir);

        Files.createDirectories(uploadPath);

        String extension =
                originalFileName.substring(
                        originalFileName.lastIndexOf("."));

        String storedFileName =
                UUID.randomUUID() + extension;

        Path newFilePath =
                uploadPath.resolve(storedFileName);

        Files.copy(
                file.getInputStream(),
                newFilePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        Path oldFilePath =
                Paths.get(resume.getFilePath());

        Files.deleteIfExists(oldFilePath);

        resume.setFileName(originalFileName);
        resume.setFileType(contentType);
        resume.setFileSize(file.getSize());
        resume.setFilePath(newFilePath.toString());
        resume.setUploadedAt(LocalDateTime.now());

        CandidateResume updatedResume =
                resumeRepository.save(resume);

        return toResponse(updatedResume);
    }

    @Transactional
    public void deleteResume(String email) throws IOException {

        CandidateProfile profile = candidateProfileRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Candidate profile not found"));

        CandidateResume resume = resumeRepository
                .findByCandidateProfileId(profile.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found"));

        Path filePath = Paths.get(resume.getFilePath());

        Files.deleteIfExists(filePath);

        resumeRepository.delete(resume);
    }

    private ResumeResponse toResponse(
            CandidateResume resume) {

        return new ResumeResponse(
                resume.getId(),
                resume.getFileName(),
                resume.getFileType(),
                resume.getFileSize(),
                resume.getUploadedAt()
        );
    }
}