
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Eye, EyeOff, BriefcaseBusiness } from "lucide-react";
import api from "../../services/api";
import "./Login.css";

function Login() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));

    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!form.email.trim() || !form.password) {
      setError("Please enter your email and password.");
      return;
    }

    setLoading(true);

    try {
      const response = await api.post("/auth/login", {
        email: form.email.trim(),
        password: form.password,
      });

      const token = response.data.token;

      if (!token) {
        setError("Login response did not contain a token.");
        return;
      }

      sessionStorage.setItem("token", token);

      navigate("/candidate/dashboard", { replace: true });
    } catch (err) {
      if (err.response) {
        if (err.response.status === 401) {
          setError("Invalid email or password.");
        } else if (err.response.status === 400) {
          setError("Please check your login details.");
        } else {
          setError(
            err.response.data?.message ||
            "Login failed. Please try again."
          );
        }
      } else {
        setError(
          "Unable to connect to the server. Please try again."
        );
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-brand">
          <div className="brand-icon">
            <BriefcaseBusiness size={27} />
          </div>
          <h1>JobPortal</h1>
          <p>Find your next opportunity</p>
        </div>

        <div className="login-heading">
          <h2>Welcome back</h2>
          <p>Sign in to continue to your account.</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email address</label>
            <input
              id="email"
              type="email"
              name="email"
              placeholder="you@example.com"
              value={form.email}
              onChange={handleChange}
              autoComplete="email"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>

            <div className="password-wrapper">
              <input
                id="password"
                type={showPassword ? "text" : "password"}
                name="password"
                placeholder="Enter your password"
                value={form.password}
                onChange={handleChange}
                autoComplete="current-password"
                required
              />

              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword((prev) => !prev)}
                aria-label={
                  showPassword ? "Hide password" : "Show password"
                }
              >
                {showPassword ? (
                  <EyeOff size={19} />
                ) : (
                  <Eye size={19} />
                )}
              </button>
            </div>
          </div>

          {error && (
            <div className="login-error" role="alert">
              {error}
            </div>
          )}

          <button
            type="submit"
            className="login-button"
            disabled={loading}
          >
            {loading ? "Signing in..." : "Sign in"}
          </button>
        </form>

        <div className="login-footer">
          Don't have an account?{" "}
          <Link to="/register">Create account</Link>
        </div>
      </div>
    </div>
  );
}

export default Login;