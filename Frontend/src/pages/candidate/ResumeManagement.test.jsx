
import {
    afterEach,
    beforeEach,
    describe,
    expect,
    it,
    vi,
} from "vitest";
import {
    render,
    screen,
    waitFor,
} from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";

import ResumeManagement from "./ResumeManagement";
import { candidateService } from "../../services/candidateService";

vi.mock("../../services/candidateService", () => ({
    candidateService: {
        getResume: vi.fn(),
        uploadResume: vi.fn(),
        replaceResume: vi.fn(),
        downloadResume: vi.fn(),
        deleteResume: vi.fn(),
    },
}));

const mockResume = {
    fileName: "resume.pdf",
    fileType: "PDF",
    fileSize: 2048,
    uploadedAt: "2026-10-25T10:00:00",
};

const renderResumeManagement = () =>
    render(
        <MemoryRouter>
            <ResumeManagement />
        </MemoryRouter>
    );

const createPdfFile = () =>
    new File(["resume content"], "resume.pdf", {
        type: "application/pdf",
    });

const createDocxFile = () =>
    new File(["resume content"], "resume.docx", {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    });

describe("ResumeManagement", () => {

    beforeEach(() => {
        vi.clearAllMocks();

        candidateService.getResume.mockResolvedValue({
            data: mockResume,
        });

        candidateService.uploadResume.mockResolvedValue({
            data: mockResume,
        });

        candidateService.replaceResume.mockResolvedValue({
            data: mockResume,
        });

        candidateService.downloadResume.mockResolvedValue({
            data: new Blob(["resume content"], {
                type: "application/pdf",
            }),
        });

        candidateService.deleteResume.mockResolvedValue({
            data: null,
        });
    });


    afterEach(() => {
        vi.restoreAllMocks();
    });

    it("displays loading state initially", () => {
        renderResumeManagement();

        expect(
            screen.getByText("Loading your resume...")
        ).toBeInTheDocument();
    });

    it("displays existing resume details", async () => {
        renderResumeManagement();

        expect(
            await screen.findByText("resume.pdf")
        ).toBeInTheDocument();

        expect(
            screen.getByText(/PDF\s*·\s*2\.0 KB/)
        ).toBeInTheDocument();
        expect(screen.getByText("Uploaded")).toBeInTheDocument();
        expect(
            screen.getByRole("button", { name: /download/i })
        ).toBeInTheDocument();
    });

    it("shows upload UI when no resume exists", async () => {
        candidateService.getResume.mockRejectedValue({
            response: { status: 404 },
        });

        renderResumeManagement();

        expect(
            await screen.findByText("You have not uploaded a resume yet.")
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", { name: /upload resume/i })
        ).toBeDisabled();

        expect(
            screen.queryByRole("button", { name: /download/i })
        ).not.toBeInTheDocument();
    });

    it("shows an error when resume loading fails", async () => {
        candidateService.getResume.mockRejectedValue({
            response: {
                status: 500,
                data: { message: "Resume service unavailable." },
            },
        });

        renderResumeManagement();

        expect(
            await screen.findByText("Resume service unavailable.")
        ).toBeInTheDocument();
    });

    it("rejects an invalid file type", async () => {
        const user = userEvent.setup({
            applyAccept: false,
        });
        candidateService.getResume.mockRejectedValue({
            response: { status: 404 },
        });

        renderResumeManagement();

        const fileInput = await screen.findByLabelText("Choose File");

        const invalidFile = new File(
            ["image content"],
            "image.png",
            { type: "image/png" }
        );

        await user.upload(fileInput, invalidFile);

        expect(
            await screen.findByText("Please select a PDF or DOCX file.")
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", { name: /upload resume/i })
        ).toBeDisabled();
    });

    it("uploads a PDF resume successfully", async () => {
        const user = userEvent.setup();

        candidateService.getResume
            .mockRejectedValueOnce({ response: { status: 404 } })
            .mockResolvedValueOnce({ data: mockResume });

        renderResumeManagement();

        const fileInput = await screen.findByLabelText("Choose File");

        await user.upload(fileInput, createPdfFile());

        expect(screen.getByText("resume.pdf")).toBeInTheDocument();

        await user.click(
            screen.getByRole("button", { name: /upload resume/i })
        );

        expect(
            await screen.findByText("Resume uploaded successfully.")
        ).toBeInTheDocument();

        expect(candidateService.uploadResume).toHaveBeenCalledOnce();
        expect(candidateService.replaceResume).not.toHaveBeenCalled();
        expect(candidateService.getResume).toHaveBeenCalledTimes(2);
    });

    it("replaces an existing resume successfully", async () => {
        const user = userEvent.setup();

        renderResumeManagement();

        const fileInput = await screen.findByLabelText("Choose File");

        await user.upload(fileInput, createDocxFile());

        await user.click(
            screen.getByRole("button", { name: /replace resume/i })
        );

        expect(
            await screen.findByText("Resume replaced successfully.")
        ).toBeInTheDocument();

        expect(candidateService.replaceResume).toHaveBeenCalledOnce();
        expect(candidateService.uploadResume).not.toHaveBeenCalled();
    });

    it("shows an error when resume upload fails", async () => {
        const user = userEvent.setup();

        candidateService.getResume.mockRejectedValue({
            response: { status: 404 },
        });

        candidateService.uploadResume.mockRejectedValue({
            response: {
                status: 413,
                data: { message: "File size exceeds the limit." },
            },
        });

        renderResumeManagement();

        const fileInput = await screen.findByLabelText("Choose File");

        await user.upload(fileInput, createPdfFile());

        await user.click(
            screen.getByRole("button", { name: /upload resume/i })
        );

        expect(
            await screen.findByText("File size exceeds the limit.")
        ).toBeInTheDocument();
    });

    it("downloads the existing resume", async () => {
        const user = userEvent.setup();

        const createObjectURL = vi.fn(() => "blob:test-url");
        const revokeObjectURL = vi.fn();

        Object.defineProperty(window.URL, "createObjectURL", {
            configurable: true,
            value: createObjectURL,
        });

        Object.defineProperty(window.URL, "revokeObjectURL", {
            configurable: true,
            value: revokeObjectURL,
        });

        renderResumeManagement();

        await screen.findByText("resume.pdf");

        const clickSpy = vi
            .spyOn(HTMLAnchorElement.prototype, "click")
            .mockImplementation(() => { });

        await user.click(
            screen.getByRole("button", { name: /download/i })
        );

        await waitFor(() => {
            expect(candidateService.downloadResume).toHaveBeenCalledOnce();
        });

        expect(createObjectURL).toHaveBeenCalledOnce();
        expect(clickSpy).toHaveBeenCalledOnce();
        expect(revokeObjectURL).toHaveBeenCalledWith("blob:test-url");
    });

    it("deletes the resume after confirmation", async () => {
        const user = userEvent.setup();

        vi.spyOn(window, "confirm").mockReturnValue(true);

        renderResumeManagement();

        await screen.findByText("resume.pdf");

        await user.click(
            screen.getByRole("button", { name: /delete/i })
        );

        expect(
            await screen.findByText("Resume deleted successfully.")
        ).toBeInTheDocument();

        expect(candidateService.deleteResume).toHaveBeenCalledOnce();
        expect(screen.queryByText("resume.pdf")).not.toBeInTheDocument();
    });

    it("does not delete the resume when confirmation is cancelled", async () => {
        const user = userEvent.setup();

        vi.spyOn(window, "confirm").mockReturnValue(false);

        renderResumeManagement();

        await screen.findByText("resume.pdf");

        await user.click(
            screen.getByRole("button", { name: /delete/i })
        );

        expect(candidateService.deleteResume).not.toHaveBeenCalled();
        expect(screen.getByText("resume.pdf")).toBeInTheDocument();
    });

    it("shows an error when resume deletion fails", async () => {
        const user = userEvent.setup();

        vi.spyOn(window, "confirm").mockReturnValue(true);

        candidateService.deleteResume.mockRejectedValue({
            response: {
                status: 500,
                data: { message: "Unable to delete resume." },
            },
        });

        renderResumeManagement();

        await screen.findByText("resume.pdf");

        await user.click(
            screen.getByRole("button", { name: /delete/i })
        );

        expect(
            await screen.findByText("Unable to delete resume.")
        ).toBeInTheDocument();

        expect(screen.getByText("resume.pdf")).toBeInTheDocument();
    });
});
