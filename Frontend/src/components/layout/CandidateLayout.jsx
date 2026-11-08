import { Outlet } from "react-router-dom";
import CandidateNavbar from "./CandidateNavbar";
import "./CandidateLayout.css";

function CandidateLayout() {
  return (
    <div className="candidate-layout">
      <CandidateNavbar />

      <main className="candidate-layout-content">
        <Outlet />
      </main>

      <footer className="candidate-layout-footer">
        <p>© {new Date().getFullYear()} JobPortal. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default CandidateLayout;