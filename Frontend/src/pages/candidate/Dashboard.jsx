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

function Dashboard() {
  const navigate = useNavigate();

  const [jobCount, setJobCount] = useState(null);
  const [resume, setResume] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [applicationCount, setApplicationCount] = useState(null);
  const [skillMatch, setSkillMatch] = useState(null);

  const fetchDashboardData = async () => {
    setLoading(true);
    setError("");

    try {
      const results = await Promise.allSettled([
        candidateService.getJobs(),
        candidateService.getResume(),
        candidateService.getMyApplications(0, 1),
        candidateService.getLatestAnalysis(),
      ]);

      const [jobsResult, resumeResult, applicationsResult, analysisResult] =
        results;

      if (jobsResult.status === "fulfilled") {
        const data = jobsResult.value.data;

        if (Array.isArray(data)) {
          setJobCount(data.length);
        } else if (Array.isArray(data?.content)) {
          setJobCount(data.totalElements ?? data.content.length);
        } else {
          setJobCount(null);
        }
      } else {
        setJobCount(null);
      }

      if (resumeResult.status === "fulfilled") {
        setResume(resumeResult.value.data);
      } else if (resumeResult.reason?.response?.status === 404) {
        setResume(null);
      } else {
        setResume(null);
      }

      if (applicationsResult.status === "fulfilled") {
        setApplicationCount(
          applicationsResult.value.data.totalElements
        );
      } else {
        setApplicationCount(null);
      }

      if (analysisResult.status === "fulfilled") {
        setSkillMatch(analysisResult.value.data.matchPercentage);
      } else if (analysisResult.reason?.response?.status === 404) {
        setSkillMatch(null);
      } else {
        setSkillMatch(null);
        console.error(
          "Failed to fetch latest resume analysis:",
          analysisResult.reason
        );
      }

      const jobsFailed = jobsResult.status === "rejected";

      const resumeFailed =
        resumeResult.status === "rejected" &&
        resumeResult.reason?.response?.status !== 404;

      const applicationsFailed =
        applicationsResult.status === "rejected";

      if (jobsFailed && resumeFailed && applicationsFailed) {
        setError("Unable to load dashboard data. Please try again.");
      }
    } catch (error) {
      setError("Something went wrong while loading your dashboard.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDashboardData();
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
      value: resume ? "Uploaded" : "Not uploaded",
      icon: <FileUser />,
      color: "purple",
    },
    {
      title: "Skill Match",
      value: skillMatch === null ? "Not analyzed" : `${skillMatch}%`,
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

        {resume && (
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