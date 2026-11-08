
import {
  MapPin,
  Briefcase,
  IndianRupee,
  CalendarDays,
  Eye,
  Building2,
  ArrowUpRight,
} from "lucide-react";

function JobCard({ job, onViewDetails }) {
  const salary = new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  }).format(job.salary);

  const deadline = job.applicationDeadline
    ? new Date(job.applicationDeadline).toLocaleDateString(
        "en-IN",
        {
          day: "numeric",
          month: "short",
          year: "numeric",
        }
      )
    : "Not specified";

  const skills = job.requiredSkills
    ?.split(",")
    .map((skill) => skill.trim())
    .filter(Boolean) ?? [];

  return (
    <div className="card job-card h-100">
      <div className="card-body">

        {/* Job Header */}
        <div className="d-flex align-items-start gap-3 mb-3">

          {/* Company Icon */}
          <div className="job-company-icon">
            <Building2 size={24} />
          </div>

          {/* Job Title */}
          <div className="flex-grow-1 min-width-0">
            <h5 className="job-title mb-1">
              {job.title}
            </h5>

            <p className="job-company-name mb-0">
              {job.recruiterName}
            </p>
          </div>

          {/* Status */}
          <span className="job-status-badge">
            {job.status}
          </span>
        </div>

        {/* Job Information */}
        <div className="job-meta-list">

          <div className="job-meta-item">
            <MapPin size={16} />
            <span>{job.location}</span>
          </div>

          <div className="job-meta-item">
            <Briefcase size={16} />
            <span>
              {job.employmentType?.replaceAll("_", " ")}
            </span>
          </div>

          <div className="job-meta-item job-salary">
            <IndianRupee size={16} />
            <span>{salary}</span>
          </div>

          <div className="job-meta-item">
            <CalendarDays size={16} />
            <span>Deadline: {deadline}</span>
          </div>

        </div>

        {/* Required Skills */}
        <div className="job-skills-section">
          <p className="job-skills-heading">
            Required Skills
          </p>

          <div className="job-skills">
            {skills.length > 0 ? (
              skills.slice(0, 4).map((skill, index) => (
                <span
                  className="job-skill-badge"
                  key={`${skill}-${index}`}
                >
                  {skill}
                </span>
              ))
            ) : (
              <span className="text-muted small">
                No skills specified
              </span>
            )}

            {skills.length > 4 && (
              <span className="job-skill-more">
                +{skills.length - 4}
              </span>
            )}
          </div>
        </div>

        {/* Card Footer */}
        <div className="job-card-footer mt-auto">
          <button
            className="btn btn-job-outline flex-grow-1"
            onClick={() => onViewDetails(job)}
          >
            <Eye size={16} className="me-2" />
            View Details
          </button>

          <button
            className="btn btn-job-primary job-card-action"
            onClick={() => onViewDetails(job)}
            title="View job and apply"
            aria-label="View job and apply"
          >
            <ArrowUpRight size={18} />
          </button>
        </div>

      </div>
    </div>
  );
}

export default JobCard;
