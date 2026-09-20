package com.jobportal.controller;

import com.jobportal.dto.ResumeResponse;
import com.jobportal.service.CandidateResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.jobportal.entity.CandidateResume;
import com.jobportal.exception.ResourceNotFoundException;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.Principal;

@RestController
@RequestMapping("/api/candidate/profile/resume")
public class CandidateResumeController {

    private final CandidateResumeService resumeService;

    public CandidateResumeController(
            CandidateResumeService resumeService) {

        this.resumeService = resumeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam(value = "file", required = false) MultipartFile file,
            Principal principal) throws IOException {

        ResumeResponse resume =
                resumeService.uploadResume(
                        file,
                        principal.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resume);
    }

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ResumeResponse> getMyResume(
            Principal principal) {

        ResumeResponse resume =
                resumeService.getMyResume(
                        principal.getName());

        return ResponseEntity.ok(resume);
    }

    @GetMapping("/download")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Resource> downloadResume(
            Principal principal) {

        CandidateResume resume =
                resumeService.getMyResumeEntity(
                        principal.getName());

        try {
            Path path = Paths.get(resume.getFilePath());

            Resource resource =
                    new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException(
                        "Resume file not found");
            }

            MediaType mediaType =
                    MediaType.parseMediaType(
                            resume.getFileType());

            ContentDisposition contentDisposition =
                    ContentDisposition.attachment()
                            .filename(resume.getFileName())
                            .build();

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .contentLength(resume.getFileSize())
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            contentDisposition.toString())
                    .body(resource);

        } catch (IOException e) {
            throw new ResourceNotFoundException(
                    "Resume file not found");
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ResumeResponse> replaceResume(
            @RequestParam(value = "file", required = false)
            MultipartFile file,
            Principal principal) throws IOException {

        ResumeResponse resume =
                resumeService.replaceResume(
                        file,
                        principal.getName());

        return ResponseEntity.ok(resume);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> deleteResume(Principal principal) throws IOException {

        resumeService.deleteResume(principal.getName());

        return ResponseEntity.noContent().build();
    }
}