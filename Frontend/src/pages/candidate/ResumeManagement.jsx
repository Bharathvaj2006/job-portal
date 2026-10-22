
import { useEffect, useRef, useState } from "react";
import {
  FileText,
  Upload,
  Download,
  Trash2,
  RefreshCw,
  CheckCircle2,
  AlertCircle,
} from "lucide-react";
import { candidateService } from "../../services/candidateService";
import "./ResumeManagement.css";

function ResumeManagement() {
  const [resume, setResume] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const fileInputRef = useRef(null);

  useEffect(() => {
    fetchResume();
  }, []);

  const fetchResume = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await candidateService.getResume();
      setResume(response.data);
    } catch (err) {
      if (err.response?.status === 404) {
        setResume(null);
      } else {
        setError(
          err.response?.data?.message || "Unable to load resume."
        );
      }
    } finally {
      setLoading(false);
    }
  };

  const handleFileChange = (event) => {
    const file = event.target.files?.[0];

    setMessage("");
    setError("");

    if (!file) {
      setSelectedFile(null);
      return;
    }

    const allowedTypes = [
      "application/pdf",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    ];

    if (!allowedTypes.includes(file.type)) {
      setError("Please select a PDF or DOCX file.");
      event.target.value = "";
      setSelectedFile(null);
      return;
    }

    setSelectedFile(file);
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setError("Please select a resume first.");
      return;
    }

    setProcessing(true);
    setError("");
    setMessage("");

    try {
      if (resume) {
        await candidateService.replaceResume(selectedFile);
        setMessage("Resume replaced successfully.");
      } else {
        await candidateService.uploadResume(selectedFile);
        setMessage("Resume uploaded successfully.");
      }

      setSelectedFile(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }

      await fetchResume();
    } catch (err) {
      setError(
        err.response?.data?.message || "Resume upload failed."
      );
    } finally {
      setProcessing(false);
    }
  };

  const handleDownload = async () => {
    setProcessing(true);
    setError("");

    try {
      const response = await candidateService.downloadResume();
      const fileUrl = window.URL.createObjectURL(
        new Blob([response.data])
      );

      const link = document.createElement("a");
      link.href = fileUrl;
      link.download = resume?.fileName || "resume";
      document.body.appendChild(link);
      link.click();
      link.remove();

      window.URL.revokeObjectURL(fileUrl);
    } catch (err) {
      setError(
        err.response?.data?.message || "Unable to download resume."
      );
    } finally {
      setProcessing(false);
    }
  };

  const handleDelete = async () => {
    const confirmed = window.confirm(
      "Are you sure you want to delete your resume?"
    );

    if (!confirmed) return;

    setProcessing(true);
    setError("");
    setMessage("");

    try {
      await candidateService.deleteResume();
      setResume(null);
      setSelectedFile(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }
      setMessage("Resume deleted successfully.");
    } catch (err) {
      setError(
        err.response?.data?.message || "Unable to delete resume."
      );
    } finally {
      setProcessing(false);
    }
  };

  const formatFileSize = (bytes) => {
    if (bytes == null) return "Unknown size";
    if (bytes < 1024) return `${bytes} bytes`;
    if (bytes < 1024 * 1024) {
      return `${(bytes / 1024).toFixed(1)} KB`;
    }
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  };

  if (loading) {
    return (
      <div className="resume-management">
        <p className="resume-loading">Loading your resume...</p>
      </div>
    );
  }

  return (
    <div className="resume-management">
      <div className="resume-page-heading">
        <div>
          <h1>Resume Management</h1>
          <p>Upload and manage the resume used for your job applications.</p>
        </div>
      </div>

      {message && (
        <div className="resume-message success">
          <CheckCircle2 size={18} />
          <span>{message}</span>
        </div>
      )}

      {error && (
        <div className="resume-message error">
          <AlertCircle size={18} />
          <span>{error}</span>
        </div>
      )}

      <section className="resume-card">
        <div className="resume-card-heading">
          <div className="resume-icon">
            <FileText size={25} />
          </div>
          <div>
            <h2>Your Resume</h2>
            <p>
              {resume
                ? "Your resume is available in your profile."
                : "You have not uploaded a resume yet."}
            </p>
          </div>
        </div>

        {resume && (
          <div className="resume-file-details">
            <div className="resume-file-icon">
              <FileText size={30} />
            </div>

            <div className="resume-file-info">
              <strong>{resume.fileName}</strong>
              <span>
                {resume.fileType || "Resume"} ·{" "}
                {formatFileSize(resume.fileSize)}
              </span>
              {resume.uploadedAt && (
                <span>
                  Uploaded:{" "}
                  {new Date(resume.uploadedAt).toLocaleDateString()}
                </span>
              )}
            </div>

            <span className="resume-status">
              <CheckCircle2 size={15} />
              Uploaded
            </span>
          </div>
        )}

        <div className="resume-upload-area">
          <div className="upload-icon">
            <Upload size={30} />
          </div>

          <h3>{resume ? "Replace your resume" : "Upload your resume"}</h3>
          <p>Choose a PDF or DOCX file from your device.</p>

          <input
            ref={fileInputRef}
            type="file"
            accept=".pdf,.docx,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            onChange={handleFileChange}
            className="resume-file-input"
            id="resume-file"
          />

          <label htmlFor="resume-file" className="choose-file-button">
            Choose File
          </label>

          {selectedFile && (
            <div className="selected-file">
              <FileText size={17} />
              <span>{selectedFile.name}</span>
            </div>
          )}

          <button
            className="resume-primary-button"
            onClick={handleUpload}
            disabled={!selectedFile || processing}
          >
            {processing ? (
              "Please wait..."
            ) : resume ? (
              <>
                <RefreshCw size={17} /> Replace Resume
              </>
            ) : (
              <>
                <Upload size={17} /> Upload Resume
              </>
            )}
          </button>
        </div>

        {resume && (
          <div className="resume-actions">
            <button
              className="resume-secondary-button"
              onClick={handleDownload}
              disabled={processing}
            >
              <Download size={17} />
              Download
            </button>

            <button
              className="resume-delete-button"
              onClick={handleDelete}
              disabled={processing}
            >
              <Trash2 size={17} />
              Delete
            </button>
          </div>
        )}
      </section>

      <div className="resume-note">
        <strong>Note:</strong> Upload a valid PDF or DOCX resume. Your
        uploaded resume can be used for AI-powered resume analysis and
        skill-gap detection.
      </div>
    </div>
  );
}

export default ResumeManagement;
