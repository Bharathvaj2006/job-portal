
import { ChevronLeft, ChevronRight } from "lucide-react";

function JobPagination({ page, totalPages, onPageChange }) {
  if (totalPages <= 1) {
    return null;
  }

  return (
    <nav aria-label="Job pagination">
      <ul className="pagination justify-content-center mt-4">

        {/* Previous */}
        <li className={`page-item ${page === 0 ? "disabled" : ""}`}>
          <button
            className="page-link"
            onClick={() => onPageChange(page - 1)}
            disabled={page === 0}
          >
            <ChevronLeft size={16} />
            Previous
          </button>
        </li>

        {/* Page Numbers */}
        {Array.from({ length: totalPages }, (_, index) => (
          <li
            key={index}
            className={`page-item ${
              page === index ? "active" : ""
            }`}
          >
            <button
              className="page-link"
              onClick={() => onPageChange(index)}
            >
              {index + 1}
            </button>
          </li>
        ))}

        {/* Next */}
        <li
          className={`page-item ${
            page === totalPages - 1 ? "disabled" : ""
          }`}
        >
          <button
            className="page-link"
            onClick={() => onPageChange(page + 1)}
            disabled={page === totalPages - 1}
          >
            Next
            <ChevronRight size={16} />
          </button>
        </li>

      </ul>
    </nav>
  );
}

export default JobPagination;
