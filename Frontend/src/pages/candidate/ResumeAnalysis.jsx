
import { useEffect, useState } from "react";
import { candidateService } from "../../services/candidateService";
import "./ResumeAnalysis.css";
import { Link } from "react-router-dom";
import getErrorMessage from "../../utils/getErrorMessage";
import {
  BriefcaseBusiness,
  BrainCircuit,
  FileSearch,
  CheckCircle2,
  Check,
  Target,
  Lightbulb,
} from "lucide-react";

function ResumeAnalysis() {
  const [jobs, setJobs] = useState([]);
  const [selectedJobId, setSelectedJobId] = useState("");
  const [analysis, setAnalysis] = useState(null);

  const [loadingJobs, setLoadingJobs] = useState(true);
  const [analyzing, setAnalyzing] = useState(false);
  const [error, setError] = useState("");
  const [jobsError, setJobsError] = useState("");


  useEffect(() => {
    const fetchPageData = async () => {
      setLoadingJobs(true);
      setError("");

      const [jobsResult, analysisResult] = await Promise.allSettled([
        candidateService.getJobs(),
        candidateService.getLatestAnalysis(),
      ]);

      if (jobsResult.status === "fulfilled") {
        const data = jobsResult.value.data;

        const jobList = Array.isArray(data)
          ? data
          : data?.content ?? [];

        setJobs(jobList);
      } else {
        console.error("Failed to load jobs:", jobsResult.reason);

        setJobsError(
          getErrorMessage(
            jobsResult.reason,
            "Unable to load available jobs."
          )
        );
      }

      if (analysisResult.status === "fulfilled") {
        const latestAnalysis = analysisResult.value.data;

        setAnalysis(latestAnalysis);

        if (latestAnalysis.jobId != null) {
          setSelectedJobId(String(latestAnalysis.jobId));
        }
      } else if (
        analysisResult.reason?.response?.status !== 404
      ) {
        console.error(
          "Failed to load latest analysis:",
          analysisResult.reason
        );

        setError((currentError) =>
          currentError
            ? `${currentError} Unable to load the latest analysis.`
            : "Unable to load the latest analysis."
        );
      }

      setLoadingJobs(false);
    };

    fetchPageData();
  }, []);


  const handleAnalyze = async () => {
    if (!selectedJobId) {
      setError("Please select a job first.");
      return;
    }

    try {
      setAnalyzing(true);
      setError("");
      setAnalysis(null);

      const response = await candidateService.analyzeResume(
        selectedJobId
      );

      setAnalysis(response.data);
    } catch (err) {
      console.error("Resume analysis failed:", err);
      setError(getErrorMessage(err, "Unable to analyze your resume. Please try again."));

    } finally {
      setAnalyzing(false);
    }
  };


  return (
    <div className="analysis-page">
      <header className="analysis-header">
        <Link to="/candidate/dashboard" className="analysis-brand">
          <BrainCircuit size={25} />
          <span>JobPortal</span>
        </Link>

        <Link to="/candidate/dashboard" className="back-link">
          Back to Dashboard
        </Link>
      </header>

      <main className="analysis-container">
        <section className="analysis-heading">
          <span className="analysis-eyebrow">AI-POWERED TOOL</span>
          <h1>Resume Analysis</h1>
          <p>
            Discover how well your skills match a job and identify
            opportunities to improve your profile.
          </p>
        </section>

        {error && (
          <div className="analysis-error" role="alert">
            {error}
          </div>
        )}

        <section className="analysis-selector">
          <div className="selector-title">
            <div className="selector-icon">
              <BriefcaseBusiness size={22} />
            </div>
            <div>
              <h2>Choose a job</h2>
              <p>Select a job to compare with your resume.</p>
            </div>
          </div>

          <label htmlFor="job-select">Available jobs</label>

          {loadingJobs ? (
            <p className="analysis-muted" role="status">
              Loading available jobs...
            </p>
          ) : jobsError ? (
            <div className="analysis-error" role="alert">
              <p>{jobsError}</p>
              <button
                type="button"
                onClick={() => window.location.reload()}
              >
                Retry
              </button>
            </div>
          ) : jobs.length === 0 ? (
            <p className="analysis-muted">
              No jobs are currently available.
            </p>
          ) : (
            <select
              id="job-select"
              value={selectedJobId}
              onChange={(e) => {
                setSelectedJobId(e.target.value);
                setAnalysis(null);
                setError("");
              }}
            >
              <option value="">Select a job</option>
              {jobs.map((job) => (
                <option key={job.id} value={job.id}>
                  {job.title}
                  {job.location ? ` — ${job.location}` : ""}
                </option>
              ))}
            </select>
          )}

          <button
            className="analyze-button"
            type="button"
            onClick={handleAnalyze}
            disabled={loadingJobs || analyzing || !selectedJobId}
          >
            <BrainCircuit size={19} />
            {analyzing ? "Analyzing Resume..." : "Analyze Resume"}
          </button>

          <p className="selector-note">
            Your uploaded resume will be compared with the selected job's
            required skills.
          </p>
        </section>

        {analyzing && (
          <div className="analysis-loading">
            <div className="loading-spinner" />
            <h3>Analyzing your resume</h3>
            <p>Identifying matched skills and skill gaps...</p>
          </div>
        )}

        {!analysis && !analyzing && !loadingJobs && (
          <section className="analysis-empty">
            <div className="empty-icon">
              <FileSearch size={32} />
            </div>
            <h2>Your analysis will appear here</h2>
            <p>
              Select a job above and click Analyze Resume to view your
              skill match, matched skills, and missing skills.
            </p>
          </section>
        )}

        {analysis && (
          <section className="results-section">
            <div className="results-heading">
              <div>
                <span className="analysis-eyebrow">YOUR RESULTS</span>
                <h2>Analysis Overview</h2>
                <p>
                  Resume comparison for{" "}
                  <strong>{analysis.jobTitle}</strong>
                </p>
              </div>
              <span className="completed-badge">
                <CheckCircle2 size={16} />
                Completed
              </span>
            </div>

            <div className="match-card">
              <div className="match-details">
                <span className="match-label">Overall Skill Match</span>
                <h3>{analysis.matchPercentage}%</h3>
                <p>
                  Match between your resume and the selected job's
                  required skills.
                </p>
              </div>

              <div
                className="match-progress"
                role="progressbar"
                aria-label="Skill match percentage"
                aria-valuenow={analysis.matchPercentage}
                aria-valuemin={0}
                aria-valuemax={100}
              >
                <div
                  className="match-progress-fill"
                  style={{
                    width: `${Math.min(
                      100,
                      Math.max(0, Number(analysis.matchPercentage) || 0)
                    )}%`,
                  }}
                />
              </div>
            </div>

            <div className="skills-grid">
              <div className="skills-card matched-card">
                <div className="skills-card-heading">
                  <div className="skills-icon matched-icon">
                    <CheckCircle2 size={21} />
                  </div>
                  <div>
                    <h3>Matched Skills</h3>
                    <p>Skills found in your resume</p>
                  </div>
                </div>

                <div className="skill-list">
                  {analysis.matchedSkills?.length ? (
                    analysis.matchedSkills.map((skill) => (
                      <span className="skill-chip matched-chip" key={skill}>
                        <Check size={14} />
                        {skill}
                      </span>
                    ))
                  ) : (
                    <p className="analysis-muted">
                      No matched skills found.
                    </p>
                  )}
                </div>
              </div>

              <div className="skills-card missing-card">
                <div className="skills-card-heading">
                  <div className="skills-icon missing-icon">
                    <Target size={21} />
                  </div>
                  <div>
                    <h3>Skill Gaps</h3>
                    <p>Skills to consider developing</p>
                  </div>
                </div>

                <div className="skill-list">
                  {analysis.missingSkills?.length ? (
                    analysis.missingSkills.map((skill) => (
                      <span className="skill-chip missing-chip" key={skill}>
                        <Target size={14} />
                        {skill}
                      </span>
                    ))
                  ) : (
                    <p className="analysis-muted">
                      No missing skills found.
                    </p>
                  )}
                </div>
              </div>
            </div>

            <div className="analysis-tip">
              <Lightbulb size={22} />
              <div>
                <h3>Next step</h3>
                <p>
                  Review your missing skills and consider developing
                  relevant experience or projects to strengthen your
                  profile for this role.
                </p>
              </div>
            </div>
          </section>
        )}
      </main>
    </div>
  );

}

export default ResumeAnalysis;
