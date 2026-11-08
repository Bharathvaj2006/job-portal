import { NavLink, useNavigate } from "react-router-dom";
import {
  LayoutDashboard,
  Briefcase,
  FileText,
  Brain,
  ClipboardList,
  LogOut,
  Menu,
  X,
  UserRound,
} from "lucide-react";
import { useState } from "react";
import { auth } from "../../utils/auth";
import "./CandidateNavbar.css";

const navigationItems = [
  {
    label: "Dashboard",
    path: "/candidate/dashboard",
    icon: LayoutDashboard,
  },
  {
    label: "Browse Jobs",
    path: "/candidate/jobs",
    icon: Briefcase,
  },
  {
    label: "My Applications",
    path: "/candidate/applications",
    icon: ClipboardList,
  },
  {
    label: "Resume Management",
    path: "/candidate/resume-management",
    icon: FileText,
  },
  {
    label: "Resume Analysis",
    path: "/candidate/resume-analysis",
    icon: Brain,
  },
  {
    label: "My Profile",
    path: "/candidate/profile",
    icon: UserRound,
  },
];

function CandidateNavbar() {
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    auth.removeToken();
    navigate("/login", { replace: true });
  };

  const closeMobileMenu = () => setMobileMenuOpen(false);

  return (
    <header className="candidate-navbar">
      <div className="candidate-navbar-container">
        <NavLink
          to="/candidate/dashboard"
          className="candidate-navbar-brand"
          onClick={closeMobileMenu}
        >
          <span className="candidate-brand-icon">
            <Briefcase size={22} />
          </span>
          <span>JobPortal</span>
        </NavLink>

        <button
          type="button"
          className="candidate-mobile-toggle"
          onClick={() => setMobileMenuOpen((prev) => !prev)}
          aria-label="Toggle navigation"
          aria-expanded={mobileMenuOpen}
        >
          {mobileMenuOpen ? <X size={22} /> : <Menu size={22} />}
        </button>

        <nav
          className={`candidate-navbar-menu ${mobileMenuOpen ? "candidate-navbar-menu-open" : ""
            }`}
        >
          <div className="candidate-navbar-links">
            {navigationItems.map(({ label, path, icon: Icon }) => (
              <NavLink
                key={path}
                to={path}
                end
                className={({ isActive }) =>
                  `candidate-navbar-link ${isActive ? "active" : ""}`
                }
                onClick={closeMobileMenu}
              >
                <Icon size={17} />
                <span>{label}</span>
              </NavLink>
            ))}
          </div>

          <button
            type="button"
            className="candidate-logout-button"
            onClick={handleLogout}
          >
            <LogOut size={17} />
            <span>Logout</span>
          </button>
        </nav>
      </div>
    </header>
  );
}

export default CandidateNavbar;