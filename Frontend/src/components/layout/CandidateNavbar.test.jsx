
import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import {
  MemoryRouter,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import CandidateNavbar from "./CandidateNavbar";
import { auth } from "../../utils/auth";

vi.mock("../../utils/auth", () => ({
  auth: {
    removeToken: vi.fn(),
  },
}));

function LocationDisplay() {
  const location = useLocation();

  return <div data-testid="current-location">{location.pathname}</div>;
}

function renderNavbar(initialRoute = "/candidate/dashboard") {
  return render(
    <MemoryRouter initialEntries={[initialRoute]}>
      <CandidateNavbar />

      <LocationDisplay />

      <Routes>
        <Route path="/login" element={<div>Login Page</div>} />
        <Route
          path="/candidate/dashboard"
          element={<div>Dashboard Page</div>}
        />
        <Route path="/candidate/jobs" element={<div>Jobs Page</div>} />
        <Route
          path="/candidate/applications"
          element={<div>Applications Page</div>}
        />
        <Route
          path="/candidate/resume-management"
          element={<div>Resume Page</div>}
        />
        <Route
          path="/candidate/resume-analysis"
          element={<div>Analysis Page</div>}
        />
        <Route
          path="/candidate/profile"
          element={<div>Profile Page</div>}
        />
      </Routes>
    </MemoryRouter>
  );
}

describe("CandidateNavbar", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders all candidate navigation links", () => {
    renderNavbar();

    expect(
      screen.getByRole("link", { name: /dashboard/i })
    ).toBeInTheDocument();

    expect(
      screen.getByRole("link", { name: /browse jobs/i })
    ).toBeInTheDocument();

    expect(
      screen.getByRole("link", { name: /my applications/i })
    ).toBeInTheDocument();

    expect(
      screen.getByRole("link", { name: /resume management/i })
    ).toBeInTheDocument();

    expect(
      screen.getByRole("link", { name: /resume analysis/i })
    ).toBeInTheDocument();

    expect(
      screen.getByRole("link", { name: /my profile/i })
    ).toBeInTheDocument();

    expect(
      screen.getByRole("button", { name: /logout/i })
    ).toBeInTheDocument();
  });

  it("navigates to Browse Jobs", () => {
    renderNavbar();

    fireEvent.click(
      screen.getByRole("link", { name: /browse jobs/i })
    );

    expect(screen.getByTestId("current-location")).toHaveTextContent(
      "/candidate/jobs"
    );

    expect(screen.getByText("Jobs Page")).toBeInTheDocument();
  });

  it("navigates to My Applications", () => {
    renderNavbar();

    fireEvent.click(
      screen.getByRole("link", { name: /my applications/i })
    );

    expect(screen.getByTestId("current-location")).toHaveTextContent(
      "/candidate/applications"
    );
  });

  it("navigates to Resume Management", () => {
    renderNavbar();

    fireEvent.click(
      screen.getByRole("link", { name: /resume management/i })
    );

    expect(screen.getByTestId("current-location")).toHaveTextContent(
      "/candidate/resume-management"
    );
  });

  it("navigates to Resume Analysis", () => {
    renderNavbar();

    fireEvent.click(
      screen.getByRole("link", { name: /resume analysis/i })
    );

    expect(screen.getByTestId("current-location")).toHaveTextContent(
      "/candidate/resume-analysis"
    );
  });

  it("navigates to My Profile", () => {
    renderNavbar();

    fireEvent.click(
      screen.getByRole("link", { name: /my profile/i })
    );

    expect(screen.getByTestId("current-location")).toHaveTextContent(
      "/candidate/profile"
    );
  });

  it("highlights the active Dashboard link", () => {
    renderNavbar("/candidate/dashboard");

    const dashboardLink = screen.getByRole("link", {
      name: /dashboard/i,
    });

    expect(dashboardLink).toHaveClass("active");
    expect(dashboardLink).toHaveAttribute("aria-current", "page");
  });

  it("highlights the active Browse Jobs link", () => {
    renderNavbar("/candidate/jobs");

    const jobsLink = screen.getByRole("link", {
      name: /browse jobs/i,
    });

    expect(jobsLink).toHaveClass("active");
    expect(jobsLink).toHaveAttribute("aria-current", "page");
  });

  it("logs out and navigates to Login", () => {
    renderNavbar();

    fireEvent.click(
      screen.getByRole("button", { name: /logout/i })
    );

    expect(auth.removeToken).toHaveBeenCalledOnce();

    expect(screen.getByTestId("current-location")).toHaveTextContent(
      "/login"
    );

    expect(screen.getByText("Login Page")).toBeInTheDocument();
  });

  it("opens the mobile menu", () => {
    renderNavbar();

    const menuButton = screen.getByRole("button", {
      name: /toggle navigation/i,
    });

    expect(menuButton).toHaveAttribute("aria-expanded", "false");

    fireEvent.click(menuButton);

    expect(menuButton).toHaveAttribute("aria-expanded", "true");
  });

  it("closes the mobile menu when toggled again", () => {
    renderNavbar();

    const menuButton = screen.getByRole("button", {
      name: /toggle navigation/i,
    });

    fireEvent.click(menuButton);
    fireEvent.click(menuButton);

    expect(menuButton).toHaveAttribute("aria-expanded", "false");
  });

  it("closes the mobile menu after clicking a navigation link", () => {
    renderNavbar();

    const menuButton = screen.getByRole("button", {
      name: /toggle navigation/i,
    });

    fireEvent.click(menuButton);

    expect(menuButton).toHaveAttribute("aria-expanded", "true");

    fireEvent.click(
      screen.getByRole("link", { name: /browse jobs/i })
    );

    expect(menuButton).toHaveAttribute("aria-expanded", "false");

    expect(screen.getByTestId("current-location")).toHaveTextContent(
      "/candidate/jobs"
    );
  });

  it("closes the mobile menu after clicking the brand", () => {
    renderNavbar();

    const menuButton = screen.getByRole("button", {
      name: /toggle navigation/i,
    });

    fireEvent.click(menuButton);

    fireEvent.click(
      screen.getByRole("link", { name: /jobportal/i })
    );

    expect(menuButton).toHaveAttribute("aria-expanded", "false");

    expect(screen.getByTestId("current-location")).toHaveTextContent(
      "/candidate/dashboard"
    );
  });
});
