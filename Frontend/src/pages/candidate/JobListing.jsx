
import { useEffect, useState } from "react";

import {
  Briefcase,
  AlertCircle,
  LoaderCircle,
  Send,
  CheckCircle2,
} from "lucide-react";

import { jobService } from "../../services/jobService";
import { applicationService } from "../../services/applicationService";

import JobCard from "../../components/jobs/JobCard";
import JobFilters from "../../components/jobs/JobFilters";
import JobPagination from "../../components/jobs/JobPagination";

import "./JobListing.css";


function JobListing() {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [filters, setFilters] = useState({
    title: "",
    location: "",
    employmentType: "",
    minSalary: "",
  });

  const [appliedFilters, setAppliedFilters] = useState({
    title: "",
    location: "",
    employmentType: "",
    minSalary: "",
  });

  const [selectedJob, setSelectedJob] = useState(null);

  const [coverLetter, setCoverLetter] = useState("");
  const [applying, setApplying] = useState(false);
  const [applicationMessage, setApplicationMessage] = useState("");
  const [applicationError, setApplicationError] = useState("");
  const [appliedJobIds, setAppliedJobIds] = useState([]);

  const handleApply = async () => {
    if (!selectedJob) return;

    setApplying(true);
    setApplicationMessage("");
    setApplicationError("");

    try {
      const application = await applicationService.applyForJob(
        selectedJob.id,
        coverLetter
      );

      setApplicationMessage(
        `Successfully applied for ${application.jobTitle}!`
      );

      setCoverLetter("");
    } catch (error) {
      setApplicationError(
        error.response?.data?.message ||
        error.response?.data?.error ||
        "Failed to apply for this job. Please try again."
      );
    } finally {
      setApplying(false);
    }
  };

  useEffect(() => {
    const fetchMyApplications = async () => {
      try {
        const response = await applicationService.getMyApplications({
          page: 0,
          size: 100,
        });

        setAppliedJobIds(
          (response.content ?? []).map((application) => application.jobId)
        );
      } catch (error) {
        console.error("Failed to load applications:", error);
      }
    };

    fetchMyApplications();
  }, []);

  // Fetch jobs whenever page or applied filters change
  useEffect(() => {
    const fetchJobs = async () => {
      setLoading(true);
      setError("");

      try {
        const params = {
          page,
          size: 9,
          ...appliedFilters,
        };

        // Remove empty filters
        Object.keys(params).forEach((key) => {
          if (params[key] === "") {
            delete params[key];
          }
        });

        const response = await jobService.getJobs(params);

        setJobs(response.content ?? []);
        setTotalPages(response.totalPages ?? 0);
        setTotalElements(response.totalElements ?? 0);
      } catch (err) {
        setJobs([]);
        setTotalPages(0);
        setTotalElements(0);

        setError(
          err.response?.data?.message ||
          err.response?.data?.error ||
          "Failed to load jobs. Please try again."
        );
      } finally {
        setLoading(false);
      }
    };

    fetchJobs();
  }, [page, appliedFilters]);

  // Update individual filter
  const handleFilterChange = (name, value) => {
    setFilters((previous) => ({
      ...previous,
      [name]: value,
    }));
  };

  // Apply filters
  const handleSearch = (event) => {
    event.preventDefault();

    setPage(0);
    setAppliedFilters({ ...filters });
  };

  // Reset filters
  const handleClear = () => {
    const emptyFilters = {
      title: "",
      location: "",
      employmentType: "",
      minSalary: "",
    };

    setFilters(emptyFilters);
    setAppliedFilters(emptyFilters);
    setPage(0);
  };

  // Change page
  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setPage(newPage);
    }
  };


  return (
    <div className="container-fluid px-lg-5 py-4 job-listing-page">

      {/* Page Header */}
      <div className="jobs-header mb-4">
        <h2>
          <Briefcase className="me-2" size={28} />
          Browse Jobs
        </h2>

        <p>
          Discover opportunities and find your next career.
        </p>
      </div>

      {/* Main Layout */}
      <div className="row g-4">

        {/* Left Sidebar - Filters */}
        <aside className="col-12 col-lg-3">
          <div className="jobs-sidebar">
            <JobFilters
              filters={filters}
              onFilterChange={handleFilterChange}
              onSearch={handleSearch}
              onClear={handleClear}
            />
          </div>
        </aside>

        {/* Right Section - Job Listings */}
        <main className="col-12 col-lg-9">

          {/* Results Header */}
          <div className="jobs-results-header">
            <div>
              <h5 className="fw-bold mb-1">
                Available Jobs
              </h5>

              {!loading && !error && (
                <p className="jobs-count mb-0">
                  Showing {jobs.length} of {totalElements} jobs
                </p>
              )}
            </div>
          </div>

          {/* Loading */}
          {loading && (
            <div className="jobs-empty-state text-center">
              <LoaderCircle
                size={40}
                className="text-primary mb-3"
              />

              <p className="text-muted">
                Loading jobs...
              </p>
            </div>
          )}

          {/* Error */}
          {!loading && error && (
            <div className="alert alert-danger d-flex align-items-center">
              <AlertCircle className="me-2" />
              {error}
            </div>
          )}

          {/* Empty State */}
          {!loading && !error && jobs.length === 0 && (
            <div className="jobs-empty-state text-center">
              <Briefcase
                size={48}
                className="text-muted mb-3"
              />

              <h5>No jobs found</h5>

              <p className="text-muted">
                Try adjusting your search filters.
              </p>

              <button
                className="btn btn-job-outline"
                onClick={handleClear}
              >
                Clear Filters
              </button>
            </div>
          )}

          {/* Job Cards */}
          {!loading && !error && jobs.length > 0 && (
            <>
              <div className="row g-3 g-xl-4">
                {jobs.map((job) => (
                  <div
                    className="col-12 col-md-6 col-xl-4"
                    key={job.id}
                  >
                    <JobCard
                      job={job}
                      onViewDetails={setSelectedJob}
                    />
                  </div>
                ))}
              </div>

              {/* Pagination */}
              <div className="job-pagination">
                <JobPagination
                  page={page}
                  totalPages={totalPages}
                  onPageChange={handlePageChange}
                />
              </div>
            </>
          )}

        </main>
      </div>

      {/* Job Details Modal */}

      {selectedJob && (
        <div className="job-modal-backdrop">
          <div
            className="job-details-modal"
            role="dialog"
            aria-modal="true"
            aria-labelledby="job-details-title"
          >
            <div className="job-details-header">
              <div>
                <h4 id="job-details-title" className="mb-1">
                  {selectedJob.title}
                </h4>
                <p className="text-muted mb-0">
                  {selectedJob.recruiterName}
                </p>
              </div>

              <button
                type="button"
                className="btn-close"
                onClick={() => {
                  setSelectedJob(null);
                  setApplicationMessage("");
                  setApplicationError("");
                }}
                aria-label="Close"
              />
            </div>

            <div className="job-details-body">
              <h6>Job Description</h6>
              <p className="job-details-description">
                {selectedJob.description}
              </p>

              <h6>Required Skills</h6>
              <div className="job-skills mb-4">
                {selectedJob.requiredSkills
                  ?.split(",")
                  .map((skill) => skill.trim())
                  .filter(Boolean)
                  .map((skill, index) => (
                    <span className="job-skill-badge" key={`${skill}-${index}`}>
                      {skill}
                    </span>
                  ))}
              </div>

              <h6>Cover Letter (Optional)</h6>
              <textarea
                className="form-control"
                rows="5"
                maxLength={5000}
                placeholder="Introduce yourself and explain why you're interested in this role..."
                value={coverLetter}
                onChange={(event) => setCoverLetter(event.target.value)}
                disabled={applying || appliedJobIds.includes(selectedJob.id)}
              />

              <div className="text-end text-muted small mt-1">
                {coverLetter.length}/5000
              </div>

              {applicationMessage && (
                <div className="alert alert-success mt-3 mb-0">
                  <CheckCircle2 size={18} className="me-2" />
                  {applicationMessage}
                </div>
              )}

              {applicationError && (
                <div className="alert alert-danger mt-3 mb-0">
                  <AlertCircle size={18} className="me-2" />
                  {applicationError}
                </div>
              )}
            </div>

            <div className="job-details-footer">
              <button
                type="button"
                className="btn btn-job-outline"
                onClick={() => setSelectedJob(null)}
                disabled={applying}
              >
                Close
              </button>

              <button
                type="button"
                className="btn btn-job-primary"
                onClick={handleApply}
                disabled={
                  applying ||
                  appliedJobIds.includes(selectedJob.id) ||
                  selectedJob.status !== "ACTIVE"
                }
              >
                {applying ? (
                  <>
                    <LoaderCircle size={16} className="me-2 spin" />
                    Applying...
                  </>
                ) : appliedJobIds.includes(selectedJob.id) ? (
                  <>
                    <CheckCircle2 size={16} className="me-2" />
                    Already Applied
                  </>
                ) : (
                  <>
                    <Send size={16} className="me-2" />
                    Apply Now
                  </>
                )}
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );

}

export default JobListing;
