import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";

import Login from "./pages/auth/Login";
import Dashboard from "./pages/candidate/Dashboard";
import ResumeAnalysis from "./pages/candidate/ResumeAnalysis";
import ResumeManagement from "./pages/candidate/ResumeManagement";
import JobListing from "./pages/candidate/JobListing";
import MyApplications from "./pages/candidate/MyApplications";
import CandidateProfile from "./pages/candidate/CandidateProfile";
import ProtectedRoute from "./components/ProtectedRoute";
import CandidateLayout from "./components/layout/CandidateLayout";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<CandidateLayout />}>
            <Route
              path="/candidate/dashboard"
              element={<Dashboard />}
            />

            <Route
              path="/candidate/jobs"
              element={<JobListing />}
            />

            <Route
              path="/candidate/applications"
              element={<MyApplications />}
            />

            <Route
              path="/candidate/resume-management"
              element={<ResumeManagement />}
            />

            <Route
              path="/candidate/resume-analysis"
              element={<ResumeAnalysis />}
            />
          </Route>
        </Route>

        <Route
          path="/"
          element={<Navigate to="/candidate/dashboard" replace />}
        />

        <Route
          path="/candidate/profile"
          element={<CandidateProfile />}
        />

        <Route
          path="*"
          element={<Navigate to="/login" replace />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;