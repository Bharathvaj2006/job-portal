import { Link, useNavigate } from "react-router-dom";
import {
  BriefcaseBusiness,Send,FileUser,ChartNoAxesCombined,Search,FileUp,BrainCircuit,UserRound,LogOut,
} from "lucide-react";
import { auth } from "../../utils/auth";
import "./Dashboard.css";

function Dashboard() {
  const navigate = useNavigate();

  const handleLogout = () => {
    auth.removeToken();
    navigate("/login", { replace: true });
  };

  const stats = [
    {
      title: "Available Jobs",
      value: "—",
      icon: <BriefcaseBusiness />,
      color: "blue",
    },
    {
      title: "Applications",
      value: "—",
      icon: <Send />,
      color: "green",
    },
    {
      title: "Resume Status",
      value: "Not checked",
      icon: <FileUser />,
      color: "purple",
    },
    {
      title: "Skill Match",
      value: "—",
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
      path: "/candidate/resume",
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

        <section className="dashboard-stats">
          {stats.map((stat) => (
            <div className="stat-card" key={stat.title}>
              <div className={`stat-icon ${stat.color}`}>
                {stat.icon}
              </div>
              <div>
                <p className="stat-title">{stat.title}</p>
                <h2>{stat.value}</h2>
              </div>
            </div>
          ))}
        </section>

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