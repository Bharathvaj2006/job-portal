import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import {
  BriefcaseBusiness,
  Send,
  FileUser,
  ChartNoAxesCombined,
  Search,
  FileUp,
  BrainCircuit,
  UserRound,
  LogOut,
} from "lucide-react";

import { auth } from "../../utils/auth";
import { candidateService } from "../../services/candidateService";
import "./Dashboard.css";
import getErrorMessage from "../../utils/getErrorMessage";

function Dashboard() {
  const navigate = useNavigate();

  const [jobCount, setJobCount] = useState(null);
  const [resume, setResume] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [applicationCount, setApplicationCount] = useState(null);
  const [skillMatch, setSkillMatch] = useState(null);
  const [resumeError, setResumeError] = useState(false);
  const [analysisError, setAnalysisError] = useState(false);


  const fetchDashboardData = async () => {
    try {
      const results = await Promise.allSettled([
        candidateService.getJobs(),
        candidateService.getResume(),
        candidateService.getMyApplications(0, 1),
        candidateService.getLatestAnalysis(),
      ]);

      setError("");
      setResumeError(false);
      setAnalysisError(false);

      const [jobsResult, resumeResult, applicationsResult, analysisResult] =
        results;

      const errors = [];

      if (jobsResult.status === "fulfilled") {
        const data = jobsResult.value.data;

        if (Array.isArray(data)) {
          setJobCount(data.length);
        } else if (Array.isArray(data?.content)) {
          setJobCount(data.totalElements ?? data.content.length);
        } else {
          setJobCount(null);
          errors.push("The jobs response was invalid.");
        }
      } else {
        setJobCount(null);
        errors.push(
          getErrorMessage(
            jobsResult.reason,
            "Unable to load available jobs."
          )
        );
      }

      if (resumeResult.status === "fulfilled") {
        setResume(resumeResult.value.data);
      } else if (resumeResult.reason?.response?.status === 404) {
        setResume(null);
      } else {
        setResume(null);
        setResumeError(true);
        errors.push(
          getErrorMessage(
            resumeResult.reason,
            "Unable to load resume status."
          )
        );
      }

      if (applicationsResult.status === "fulfilled") {
        const data = applicationsResult.value.data;

        setApplicationCount(
          data?.totalElements ??
          data?.content?.length ??
          null
        );
      } else {
        setApplicationCount(null);
        errors.push(
          getErrorMessage(
            applicationsResult.reason,
            "Unable to load application count."
          )
        );
      }

      if (analysisResult.status === "fulfilled") {
        setSkillMatch(
          analysisResult.value.data?.matchPercentage ?? null
        );
      } else if (analysisResult.reason?.response?.status === 404) {
        setSkillMatch(null);
      } else {
        setSkillMatch(null);
        setAnalysisError(true);

        errors.push(
          getErrorMessage(
            analysisResult.reason,
            "Unable to load the latest resume analysis."
          )
        );

        console.error(
          "Failed to fetch latest resume analysis:",
          analysisResult.reason
        );
      }

      setError([...new Set(errors)].join(" "));
    } catch (err) {
      setError(
        getErrorMessage(
          err,
          "Something went wrong while loading your dashboard."
        )
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      fetchDashboardData();
    }, 0);

    return () => clearTimeout(timeoutId);
  }, []);

  const handleLogout = () => {
    auth.removeToken();
    navigate("/login", { replace: true });
  };

  const stats = [
    {
      title: "Available Jobs",
      value: jobCount === null ? "—" : jobCount,
      icon: <BriefcaseBusiness />,
      color: "blue",
    },
    {
      title: "Applications",
      value: applicationCount === null ? "—" : applicationCount,
      icon: <Send />,
      color: "green",
    },
    {
      title: "Resume Status",
      value: resumeError
        ? "Unavailable"
        : resume
          ? "Uploaded"
          : "Not uploaded",
      icon: <FileUser />,
      color: "purple",
    },
    {
      title: "Skill Match",
      value: analysisError
        ? "Unavailable"
        : skillMatch === null
          ? "Not analyzed"
          : `${skillMatch}%`,
      icon: <ChartNoAxesCombined />,
      color: "orange",
    },
  ];

  const actions = [
    {
      title: "Browse Jobs",
      description: "Find jobs that match your skills",
      icon: <Search />,
      path: "/candidate/jobs",
    },
    {
      title: "Manage Resume",
      description: "Upload or update your resume",
      icon: <FileUp />,
      path: "/candidate/resume-management",
    },
    {
      title: "Analyze Resume",
      description: "Discover your skill match and gaps",
      icon: <BrainCircuit />,
      path: "/candidate/resume-analysis",
    },
    {
      title: "My Profile",
      description: "View and update your profile",
      icon: <UserRound />,
      path: "/candidate/profile",
    },
  ];

  return (
    <div className="dashboard-page">
      <header className="dashboard-header">
        <Link to="/candidate/dashboard" className="dashboard-brand">
          JobPortal
        </Link>

        <div className="dashboard-header-right">
          <span className="candidate-label">Candidate</span>

          <button className="logout-button" onClick={handleLogout}>
            <LogOut size={17} />
            Logout
          </button>
        </div>
      </header>

      <main className="dashboard-container">
        <section className="dashboard-welcome">
          <div>
            <h1>Candidate Dashboard</h1>
            <p>
              Welcome back! Here's an overview of your job search.
            </p>
          </div>
        </section>

        {error && (
          <div className="dashboard-error" role="alert">
            {error}
          </div>
        )}

        <section className="dashboard-stats">
          {stats.map((stat) => (
            <div className="stat-card" key={stat.title}>
              <div className={`stat-icon ${stat.color}`}>
                {stat.icon}
              </div>

              <div>
                <p className="stat-title">{stat.title}</p>
                <h2>
                  {loading
                    ? "..."
                    : stat.value}
                </h2>
              </div>
            </div>
          ))}
        </section>

        {resume && !resumeError && (
          <section className="resume-summary">
            <div>
              <h2>Your Resume</h2>
              <p>{resume.fileName}</p>
            </div>
            <span className="resume-badge">Uploaded</span>
          </section>
        )}

        <section className="dashboard-section">
          <h2>Quick Actions</h2>

          <div className="action-grid">
            {actions.map((action) => (
              <Link
                to={action.path}
                className="action-card"
                key={action.title}
              >
                <div className="action-icon">{action.icon}</div>

                <div className="action-content">
                  <h3>{action.title}</h3>
                  <p>{action.description}</p>
                </div>
              </Link>
            ))}
          </div>
        </section>
      </main>
    </div>
  );
}

export default Dashboard;