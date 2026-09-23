package com.jobportal.service;

import com.jobportal.entity.CandidateResume;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResumeTextExtractorTest {

    private final ResumeTextExtractor extractor = new ResumeTextExtractor();

    @Test
    void shouldExtractTextFromPdf() throws Exception {

        Path pdfPath = Files.createTempFile(
                        "resume-test-",
                        ".pdf"
                        );

        try {

            createTestPdf(pdfPath);

            CandidateResume resume =
                    new CandidateResume(
                            "resume.pdf",
                            "application/pdf",
                            Files.size(pdfPath),
                            pdfPath.toString(),
                            LocalDateTime.now(),
                            null
                    );

            String extractedText =
                    extractor.extractText(resume);

            assertTrue(
                    extractedText.contains("Java"),
                    "Extracted text should contain Java"
            );

            assertTrue(
                    extractedText.contains("Spring Boot"),
                    "Extracted text should contain Spring Boot"
            );

        } finally {

            Files.deleteIfExists(pdfPath);
        }
    }

    @Test
    void shouldExtractTextFromDocx() throws Exception {

        Path docxPath =
                Files.createTempFile(
                        "resume-test-",
                        ".docx"
                );

        try {

            createTestDocx(docxPath);

            CandidateResume resume =
                    new CandidateResume(
                            "resume.docx",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                            Files.size(docxPath),
                            docxPath.toString(),
                            LocalDateTime.now(),
                            null
                    );

            String extractedText =
                    extractor.extractText(resume);

            assertTrue(
                    extractedText.contains("Java"),
                    "Extracted text should contain Java"
            );

            assertTrue(
                    extractedText.contains("Spring Boot"),
                    "Extracted text should contain Spring Boot"
            );

        } finally {

            Files.deleteIfExists(docxPath);
        }
    }

    private void createTestPdf(Path path)
            throws Exception {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();

            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                contentStream.beginText();

                contentStream.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA),
                        12
                );

                contentStream.newLineAtOffset(
                        50,
                        700
                );

                contentStream.showText(
                        "Java Spring Boot Developer"
                );

                contentStream.newLineAtOffset(
                        0,
                        -20
                );

                contentStream.showText(
                        "Skills: Java, Spring Boot, PostgreSQL"
                );

                contentStream.endText();
            }

            document.save(path.toFile());
        }
    }

    private void createTestDocx(Path path)
            throws Exception {

        try (XWPFDocument document =
                     new XWPFDocument()) {

            XWPFParagraph paragraph =
                    document.createParagraph();

            paragraph.createRun()
                    .setText(
                            "Java Spring Boot Developer"
                    );

            XWPFParagraph skillsParagraph =
                    document.createParagraph();

            skillsParagraph.createRun()
                    .setText(
                            "Skills: Java, Spring Boot, PostgreSQL"
                    );

            try (OutputStream outputStream =
                         Files.newOutputStream(path)) {

                document.write(outputStream);
            }
        }
    }
}