
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

import Dashboard from "./Dashboard";
import { candidateService } from "../../services/candidateService";

const { mockedNavigate } = vi.hoisted(() => ({
  mockedNavigate: vi.fn(),
}));

vi.mock("react-router-dom", async (importOriginal) => {
  const actual = await importOriginal();

  return {
    ...actual,
    useNavigate: () => mockedNavigate,
  };
});

vi.mock("../../services/candidateService", () => ({
  candidateService: {
    getJobs: vi.fn(),
    getResume: vi.fn(),
    getMyApplications: vi.fn(),
    getLatestAnalysis: vi.fn(),
  },
}));

vi.mock("../../utils/auth", () => ({
  auth: {
    removeToken: vi.fn(),
  },
}));

const renderDashboard = () =>
  render(
    <MemoryRouter>
      <Dashboard />
    </MemoryRouter>
  );

const mockSuccessfulResponses = () => {
  candidateService.getJobs.mockResolvedValue({
    data: [{ id: 1 }, { id: 2 }],
  });

  candidateService.getResume.mockResolvedValue({
    data: { fileName: "resume.pdf" },
  });

  candidateService.getMyApplications.mockResolvedValue({
    data: { totalElements: 3 },
  });

  candidateService.getLatestAnalysis.mockResolvedValue({
    data: { matchPercentage: 85 },
  });
};

describe("Dashboard", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockSuccessfulResponses();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it("displays the dashboard loading state", () => {
    renderDashboard();

    expect(screen.getAllByText("...")).toHaveLength(4);
  });

  it("displays dashboard statistics when API requests succeed", async () => {
    renderDashboard();

    await waitFor(() => {
      expect(screen.getByText("85%")).toBeInTheDocument();
    });

    expect(screen.getByText("2")).toBeInTheDocument();
    expect(screen.getByText("3")).toBeInTheDocument();
    expect(screen.getAllByText("Uploaded")).toHaveLength(2);
    expect(screen.getByText("resume.pdf")).toBeInTheDocument();
  });

  it("shows Not uploaded when the candidate has no resume", async () => {
    candidateService.getResume.mockRejectedValue({
      response: { status: 404 },
    });

    renderDashboard();

    await waitFor(() => {
      expect(screen.getByText("Not uploaded")).toBeInTheDocument();
    });

    expect(screen.queryByText("resume.pdf")).not.toBeInTheDocument();
    expect(screen.queryByRole("alert")).not.toBeInTheDocument();
  });

  it("shows Not analyzed when no previous analysis exists", async () => {
    candidateService.getLatestAnalysis.mockRejectedValue({
      response: { status: 404 },
    });

    renderDashboard();

    await waitFor(() => {
      expect(screen.getByText("Not analyzed")).toBeInTheDocument();
    });

    expect(screen.getByText("2")).toBeInTheDocument();
  });

  it("shows unavailable statuses when resume and analysis requests fail", async () => {
    vi.spyOn(console, "error").mockImplementation(() => {});

    candidateService.getResume.mockRejectedValue({
      response: {
        status: 500,
        data: { message: "Unable to load resume status." },
      },
    });

    candidateService.getLatestAnalysis.mockRejectedValue({
      response: {
        status: 500,
        data: { message: "Analysis service unavailable." },
      },
    });

    renderDashboard();

    await waitFor(() => {
        expect(screen.getAllByText("Unavailable")).toHaveLength(2);
    });

    expect(screen.getAllByText("Unavailable")).toHaveLength(2);
    expect(screen.getByText("2")).toBeInTheDocument();
    expect(screen.getByText("3")).toBeInTheDocument();

    const alert = screen.getByRole("alert");
    expect(alert).toHaveTextContent("Unable to load resume status.");
    expect(alert).toHaveTextContent("Analysis service unavailable.");
  });

  it("shows an error when the jobs request fails but keeps other data", async () => {
    candidateService.getJobs.mockRejectedValue({
      response: {
        status: 500,
        data: { message: "Jobs service unavailable." },
      },
    });

    renderDashboard();

    await waitFor(() => {
      expect(
        screen.getByRole("alert")
      ).toHaveTextContent("Jobs service unavailable.");
    });

    expect(screen.getByText("3")).toBeInTheDocument();
    expect(screen.getByText("85%")).toBeInTheDocument();
  });

});
