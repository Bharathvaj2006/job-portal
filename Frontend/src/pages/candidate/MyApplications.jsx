
import { useCallback, useEffect, useState } from "react";
import {
    Briefcase,
    CalendarDays,
    FileText,
    LoaderCircle,
    AlertCircle,
    CheckCircle2,
    Clock,
    XCircle,
    UserCheck,
    Undo2,
    RefreshCw,
} from "lucide-react";

import { applicationService } from "../../services/applicationService";
import JobPagination from "../../components/jobs/JobPagination";
import "./MyApplications.css";

const statusConfig = {
    APPLIED: {
        label: "Applied",
        className: "application-status-applied",
        icon: Clock,
    },
    SHORTLISTED: {
        label: "Shortlisted",
        className: "application-status-shortlisted",
        icon: CheckCircle2,
    },
    REJECTED: {
        label: "Rejected",
        className: "application-status-rejected",
        icon: XCircle,
    },
    HIRED: {
        label: "Hired",
        className: "application-status-hired",
        icon: UserCheck,
    },
    WITHDRAWN: {
        label: "Withdrawn",
        className: "application-status-withdrawn",
        icon: Undo2,
    },
};

function MyApplications() {
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [withdrawingId, setWithdrawingId] = useState(null);
    const [message, setMessage] = useState("");
    const [withdrawError, setWithdrawError] = useState("");

    const fetchApplications = useCallback(async () => {
        setLoading(true);
        setError("");

        try {
            const response = await applicationService.getMyApplications({
                page,
                size: 9,
                sortBy: "appliedAt",
                direction: "desc",
            });

            setApplications(response.content ?? []);
            setTotalPages(response.totalPages ?? 0);
            setTotalElements(response.totalElements ?? 0);
        } catch (err) {
            setApplications([]);
            setTotalPages(0);
            setTotalElements(0);
            setError(
                err.response?.data?.message ||
                err.response?.data?.error ||
                "Failed to load applications. Please try again."
            );
        } finally {
            setLoading(false);
        }
    }, [page]);

    useEffect(() => {
        const timer = setTimeout(() => {
            fetchApplications();
        }, 0);

        return () => clearTimeout(timer);
    }, [fetchApplications]);

    const handleWithdraw = async (application) => {
        const confirmed = window.confirm(
            `Are you sure you want to withdraw your application for "${application.jobTitle}"?`
        );

        if (!confirmed) return;

        setWithdrawingId(application.id);
        setMessage("");
        setWithdrawError("");

        try {
            await applicationService.withdrawApplication(application.id);

            setMessage(`Application for "${application.jobTitle}" withdrawn successfully.`);

            await fetchApplications();
        } catch (err) {
            setWithdrawError(
                err.response?.data?.message ||
                err.response?.data?.error ||
                "Failed to withdraw application."
            );
        } finally {
            setWithdrawingId(null);
        }
    };

    const formatDate = (date) => {
        if (!date) return "Not available";

        return new Date(date).toLocaleDateString("en-IN", {
            day: "numeric",
            month: "short",
            year: "numeric",
        });
    };

    return (
        <div className="container-fluid px-lg-5 py-4 my-applications-page">
            <div className="applications-header">
                <div>
                    <span className="applications-eyebrow">CANDIDATE PORTAL</span>
                    <h2>My Applications</h2>
                    <p>Track your job applications and their current status.</p>
                </div>

                <button
                    type="button"
                    className="btn btn-job-outline"
                    onClick={fetchApplications}
                    disabled={loading}
                >
                    <RefreshCw size={16} className={`me-2 ${loading ? "spin" : ""}`} />
                    Refresh
                </button>
            </div>

            {message && (
                <div className="alert alert-success">
                    <CheckCircle2 size={18} className="me-2" />
                    {message}
                </div>
            )}

            {withdrawError && (
                <div className="alert alert-danger">
                    <AlertCircle size={18} className="me-2" />
                    {withdrawError}
                </div>
            )}

            <div className="applications-summary">
                <Briefcase size={20} />
                <span>
                    <strong>{totalElements}</strong> application
                    {totalElements !== 1 ? "s" : ""} submitted
                </span>
            </div>

            {loading ? (
                <div className="applications-loading">
                    <LoaderCircle size={32} className="spin text-primary" />
                    <p>Loading your applications...</p>
                </div>
            ) : error ? (
                <div className="alert alert-danger">
                    <AlertCircle size={18} className="me-2" />
                    {error}
                    <button
                        type="button"
                        className="btn btn-sm btn-outline-danger ms-3"
                        onClick={fetchApplications}
                    >
                        Retry
                    </button>
                </div>
            ) : applications.length === 0 ? (
                <div className="applications-empty">
                    <div className="applications-empty-icon">
                        <Briefcase size={36} />
                    </div>
                    <h4>No applications yet</h4>
                    <p>
                        Once you apply for a job, it will appear here.
                    </p>
                    <a href="/candidate/jobs" className="btn btn-job-primary">
                        Browse Jobs
                    </a>
                </div>
            ) : (
                <>
                    <div className="applications-list">
                        {applications.map((application) => {
                            const status =
                                statusConfig[application.status] ?? statusConfig.APPLIED;
                            const StatusIcon = status.icon;

                            return (
                                <article
                                    className="application-card"
                                    key={application.id}
                                >
                                    <div className="application-card-main">
                                        <div className="application-company-icon">
                                            <Briefcase size={22} />
                                        </div>

                                        <div className="application-info">
                                            <h5>{application.jobTitle}</h5>

                                            <div className="application-meta">
                                                <span>
                                                    <CalendarDays size={15} />
                                                    Applied {formatDate(application.appliedAt)}
                                                </span>

                                                <span>
                                                    <FileText size={15} />
                                                    {application.resumeFileName || "Resume submitted"}
                                                </span>
                                            </div>
                                        </div>

                                        <span className={`application-status ${status.className}`}>
                                            <StatusIcon size={15} />
                                            {status.label}
                                        </span>
                                    </div>

                                    <div className="application-card-footer">
                                        <span className="application-id">
                                            Application #{application.id}
                                        </span>

                                        {application.status === "APPLIED" && (
                                            <button
                                                type="button"
                                                className="btn btn-sm btn-outline-danger"
                                                onClick={() => handleWithdraw(application)}
                                                disabled={withdrawingId === application.id}
                                            >
                                                {withdrawingId === application.id ? (
                                                    <>
                                                        <LoaderCircle size={15} className="me-1 spin" />
                                                        Withdrawing...
                                                    </>
                                                ) : (
                                                    <>
                                                        <Undo2 size={15} className="me-1" />
                                                        Withdraw
                                                    </>
                                                )}
                                            </button>
                                        )}
                                    </div>
                                </article>
                            );
                        })}
                    </div>

                    {totalPages > 1 && (
                        <div className="job-pagination mt-4">
                            <JobPagination
                                page={page}
                                totalPages={totalPages}
                                onPageChange={setPage}
                            />
                        </div>
                    )}
                </>
            )}
        </div>
    );
}

export default MyApplications;
