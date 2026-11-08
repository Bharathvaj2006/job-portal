
import { Search, RotateCcw, SlidersHorizontal } from "lucide-react";

function JobFilters({ filters, onFilterChange, onSearch, onClear }) {
  return (
    <div className="card shadow-sm job-filters">
      <div className="card-body">

        {/* Filter Header */}
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h5 className="mb-0">
            <SlidersHorizontal
              size={18}
              className="me-2 text-primary"
            />
            Filters
          </h5>

          <button
            type="button"
            className="btn btn-sm btn-link text-decoration-none p-0"
            onClick={onClear}
          >
            Clear All
          </button>
        </div>

        <form onSubmit={onSearch}>
          <div className="row g-3">

            {/* Job Title */}
            <div className="col-md-6">
              <label className="form-label fw-semibold">
                Job Title
              </label>

              <input
                type="text"
                className="form-control"
                placeholder="e.g. Developer"
                value={filters.title}
                onChange={(e) =>
                  onFilterChange("title", e.target.value)
                }
              />
            </div>

            {/* Location */}
            <div className="col-md-6">
              <label className="form-label fw-semibold">
                Location
              </label>

              <input
                type="text"
                className="form-control"
                placeholder="e.g. Chennai"
                value={filters.location}
                onChange={(e) =>
                  onFilterChange("location", e.target.value)
                }
              />
            </div>

            {/* Employment Type */}
            <div className="col-md-6">
              <label className="form-label fw-semibold">
                Employment Type
              </label>

              <select
                className="form-select"
                value={filters.employmentType}
                onChange={(e) =>
                  onFilterChange("employmentType", e.target.value)
                }
              >
                <option value="">All Types</option>
                <option value="FULL_TIME">Full Time</option>
                <option value="PART_TIME">Part Time</option>
                <option value="CONTRACT">Contract</option>
                <option value="INTERNSHIP">Internship</option>
              </select>
            </div>

            {/* Minimum Salary */}
            <div className="col-md-6">
              <label className="form-label fw-semibold">
                Minimum Salary (₹)
              </label>

              <input
                type="number"
                className="form-control"
                placeholder="e.g. 500000"
                min="0"
                value={filters.minSalary}
                onChange={(e) =>
                  onFilterChange("minSalary", e.target.value)
                }
              />
            </div>

            {/* Search Button */}
            <div className="col-12">
              <button
                type="submit"
                className="btn btn-job-primary w-100"
              >
                <Search size={16} className="me-2" />
                Search Jobs
              </button>
            </div>

            {/* Clear Filters Button */}
            <div className="col-12">
              <button
                type="button"
                className="btn btn-job-outline w-100"
                onClick={onClear}
              >
                <RotateCcw size={16} className="me-2" />
                Clear Filters
              </button>
            </div>

          </div>
        </form>

      </div>
    </div>
  );
}

export default JobFilters;
