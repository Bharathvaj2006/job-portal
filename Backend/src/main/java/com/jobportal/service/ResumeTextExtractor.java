package com.jobportal.service;

import com.jobportal.entity.CandidateResume;
import com.jobportal.exception.ResourceNotFoundException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ResumeTextExtractor {

    public String extractText(CandidateResume resume)
            throws IOException {

        Path filePath =
                Paths.get(resume.getFilePath());

        if (!Files.exists(filePath)
                || !Files.isReadable(filePath)) {

            throw new ResourceNotFoundException(
                    "Resume file not found");
        }

        String fileType = resume.getFileType();

        if ("application/pdf".equals(fileType)) {

            return extractPdfText(filePath);

        } else if (
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        .equals(fileType)) {

            return extractDocxText(filePath);

        } else {

            throw new IllegalArgumentException(
                    "Unsupported resume format");
        }
    }

    private String extractPdfText(Path filePath)
            throws IOException {

        byte[] fileBytes =
                Files.readAllBytes(filePath);

        try (PDDocument document =
                     Loader.loadPDF(fileBytes)) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            return stripper.getText(document);
        }
    }

    private String extractDocxText(Path filePath)
            throws IOException {

        StringBuilder text =
                new StringBuilder();

        try (InputStream inputStream =
                     Files.newInputStream(filePath);
             XWPFDocument document =
                     new XWPFDocument(inputStream)) {

            for (XWPFParagraph paragraph :
                    document.getParagraphs()) {

                String paragraphText =
                        paragraph.getText();

                if (paragraphText != null
                        && !paragraphText.isBlank()) {

                    text.append(paragraphText)
                            .append(System.lineSeparator());
                }
            }
        }

        return text.toString();
    }
}