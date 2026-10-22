
import {
  BrowserRouter,
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import Login from "./pages/auth/Login";
import Dashboard from "./pages/candidate/Dashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import ResumeAnalysis from "./pages/candidate/ResumeAnalysis";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route element={<ProtectedRoute />}>
          <Route
            path="/candidate/dashboard"
            element={<Dashboard />}
          />
        </Route>

        <Route
          path="/"
          element={<Navigate to="/candidate/dashboard" replace />}
        />

        <Route
          path="/candidate/resume-analysis"
          element={<ResumeAnalysis />}
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