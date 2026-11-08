import { useCallback, useEffect, useState } from "react";
import {
    UserRound,
    MapPin,
    Phone,
    Mail,
    BriefcaseBusiness,
    FileText,
    Pencil,
    Save,
    X,
    LoaderCircle,
    AlertCircle,
    CheckCircle2,
    Plus,
    Code2,
} from "lucide-react";

import { candidateProfileService } from "../../services/candidateProfileService";
import getErrorMessage from "../../utils/getErrorMessage";
import "./CandidateProfile.css";

const initialForm = {
    phone: "",
    location: "",
    headline: "",
    summary: "",
};

function CandidateProfile() {
    const [profile, setProfile] = useState(null);
    const [skills, setSkills] = useState([]);
    const [selectedSkillIds, setSelectedSkillIds] = useState([]);
    const [savedSkills, setSavedSkills] = useState([]);

    const [form, setForm] = useState(initialForm);
    const [editing, setEditing] = useState(false);
    const [profileExists, setProfileExists] = useState(false);

    const [loading, setLoading] = useState(true);
    const [savingProfile, setSavingProfile] = useState(false);
    const [savingSkills, setSavingSkills] = useState(false);

    const [error, setError] = useState("");
    const [profileError, setProfileError] = useState("");
    const [skillsError, setSkillsError] = useState("");
    const [message, setMessage] = useState("");

    const fetchProfile = useCallback(async () => {
        setLoading(true);
        setError("");

        try {
            const [profileResult, skillsResult] = await Promise.allSettled([
                candidateProfileService.getMyProfile(),
                candidateProfileService.getAllSkills(),
            ]);

            if (skillsResult.status === "fulfilled") {
                setSkills(skillsResult.value);
            } else {
                setSkillsError(
                    getErrorMessage(skillsResult.reason, "Unable to load skills.")
                );
            }

            if (profileResult.status === "fulfilled") {
                const data = profileResult.value;

                setProfile(data);
                setProfileExists(true);
                setSavedSkills(data.skills ?? []);

                setForm({
                    phone: data.phone ?? "",
                    location: data.location ?? "",
                    headline: data.headline ?? "",
                    summary: data.summary ?? "",
                });

                const matchedSkillIds = skillsResult.status === "fulfilled"
                    ? skillsResult.value
                        .filter((skill) => (data.skills ?? []).includes(skill.name))
                        .map((skill) => skill.id)
                    : [];

                setSelectedSkillIds(matchedSkillIds);
            } else {
                const status = profileResult.reason?.response?.status;

                if (status === 404) {
                    setProfile(null);
                    setProfileExists(false);
                    setEditing(true);
                    setForm(initialForm);
                    setSavedSkills([]);
                    setSelectedSkillIds([]);
                } else {
                    setError(
                        getErrorMessage(
                            profileResult.reason,
                            "Unable to load your profile."
                        )
                    );
                }
            }
        } catch (err) {
            setError(getErrorMessage(err, "Unable to load your profile."));
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        const timer = setTimeout(() => {
            fetchProfile();
        }, 0);

        return () => clearTimeout(timer);
    }, [fetchProfile]);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setForm((previous) => ({
            ...previous,
            [name]: value,
        }));
    };

    const validateProfile = () => {
        if (!/^[0-9]{10}$/.test(form.phone.trim())) {
            setProfileError("Phone must contain exactly 10 digits.");
            return false;
        }

        if (!form.location.trim()) {
            setProfileError("Location is required.");
            return false;
        }

        if (!form.headline.trim()) {
            setProfileError("Professional headline is required.");
            return false;
        }

        if (form.headline.trim().length > 100) {
            setProfileError("Headline cannot exceed 100 characters.");
            return false;
        }

        if (form.summary.length > 2000) {
            setProfileError("Summary cannot exceed 2000 characters.");
            return false;
        }

        setProfileError("");
        return true;
    };

    const handleSaveProfile = async (event) => {
        event.preventDefault();

        if (!validateProfile()) {
            return;
        }

        setSavingProfile(true);
        setProfileError("");
        setMessage("");

        const payload = {
            phone: form.phone.trim(),
            location: form.location.trim(),
            headline: form.headline.trim(),
            summary: form.summary.trim(),
        };

        try {
            const response = profileExists
                ? await candidateProfileService.updateProfile(payload)
                : await (async () => {
                    await candidateProfileService.createProfile(payload);
                    return candidateProfileService.getMyProfile();
                })();

            setProfile(response);
            setProfileExists(true);
            setSavedSkills(response.skills ?? []);
            setEditing(false);

            setMessage(
                profileExists
                    ? "Profile updated successfully."
                    : "Profile created successfully."
            );
        } catch (err) {
            setProfileError(
                getErrorMessage(err, "Unable to save your profile.")
            );
        } finally {
            setSavingProfile(false);
        }
    };

    const handleCancelEdit = () => {
        if (profileExists && profile) {
            setForm({
                phone: profile.phone ?? "",
                location: profile.location ?? "",
                headline: profile.headline ?? "",
                summary: profile.summary ?? "",
            });

            setEditing(false);
        } else {
            setForm(initialForm);
        }

        setProfileError("");
    };

    const handleSkillToggle = (skillId) => {
        setSelectedSkillIds((previous) =>
            previous.includes(skillId)
                ? previous.filter((id) => id !== skillId)
                : [...previous, skillId]
        );
    };

    const handleSaveSkills = async () => {
        setSavingSkills(true);
        setSkillsError("");
        setMessage("");

        try {
            const response =
                await candidateProfileService.updateSkills(selectedSkillIds);

            setProfile(response);
            setSavedSkills(response.skills ?? []);
            setMessage("Skills updated successfully.");
        } catch (err) {
            setSkillsError(
                getErrorMessage(err, "Unable to update skills.")
            );
        } finally {
            setSavingSkills(false);
        }
    };

    if (loading) {
        return (
            <div className="candidate-profile-loading">
                <LoaderCircle size={32} className="profile-spin" />
                <p>Loading your profile...</p>
            </div>
        );
    }

    return (
        <div className="candidate-profile-page">
            <div className="candidate-profile-header">
                <div>
                    <span className="candidate-profile-eyebrow">
                        ACCOUNT
                    </span>
                    <h1>My Profile</h1>
                    <p>Manage your professional information and skills.</p>
                </div>

                {profileExists && !editing && (
                    <button
                        type="button"
                        className="profile-primary-button"
                        onClick={() => {
                            setEditing(true);
                            setMessage("");
                        }}
                    >
                        <Pencil size={17} />
                        Edit Profile
                    </button>
                )}
            </div>

            {error && (
                <div className="profile-alert profile-alert-error">
                    <AlertCircle size={18} />
                    <span>{error}</span>
                    <button type="button" onClick={fetchProfile}>
                        Retry
                    </button>
                </div>
            )}

            {message && (
                <div className="profile-alert profile-alert-success">
                    <CheckCircle2 size={18} />
                    <span>{message}</span>
                </div>
            )}

            {!error && (
                <>
                    {!profileExists || editing ? (
                        <section className="profile-panel">
                            <div className="profile-panel-heading">
                                <div className="profile-heading-icon">
                                    <UserRound size={20} />
                                </div>
                                <div>
                                    <h2>{profileExists ? "Edit Profile" : "Create Profile"}</h2>
                                    <p>
                                        {profileExists
                                            ? "Update your professional details."
                                            : "Complete your profile to start applying for jobs."}
                                    </p>
                                </div>
                            </div>

                            <form onSubmit={handleSaveProfile}>
                                <div className="profile-form-grid">
                                    <div className="profile-form-field">
                                        <label htmlFor="profile-phone">Phone Number *</label>
                                        <input
                                            id="profile-phone"
                                            name="phone"
                                            value={form.phone}
                                            onChange={handleChange}
                                            placeholder="10-digit phone number"
                                            maxLength={10}
                                            inputMode="numeric"
                                            required
                                        />
                                    </div>

                                    <div className="profile-form-field">
                                        <label htmlFor="profile-location">Location *</label>
                                        <input
                                            id="profile-location"
                                            name="location"
                                            value={form.location}
                                            onChange={handleChange}
                                            placeholder="e.g. Chennai, India"
                                            required
                                        />
                                    </div>

                                    <div className="profile-form-field profile-form-full">
                                        <label htmlFor="profile-headline">
                                            Professional Headline *
                                        </label>
                                        <input
                                            id="profile-headline"
                                            name="headline"
                                            value={form.headline}
                                            onChange={handleChange}
                                            placeholder="e.g. Java Backend Developer"
                                            maxLength={100}
                                            required
                                        />
                                        <small>{form.headline.length}/100 characters</small>
                                    </div>

                                    <div className="profile-form-field profile-form-full">
                                        <label htmlFor="profile-summary">
                                            Professional Summary
                                        </label>
                                        <textarea
                                            id="profile-summary"
                                            name="summary"
                                            value={form.summary}
                                            onChange={handleChange}
                                            placeholder="Briefly describe your experience, strengths, and career goals..."
                                            rows={6}
                                            maxLength={2000}
                                        />
                                        <small>{form.summary.length}/2000 characters</small>
                                    </div>
                                </div>

                                {profileError && (
                                    <div className="profile-inline-error">
                                        <AlertCircle size={16} />
                                        {profileError}
                                    </div>
                                )}

                                <div className="profile-form-actions">
                                    {profileExists && (
                                        <button
                                            type="button"
                                            className="profile-secondary-button"
                                            onClick={handleCancelEdit}
                                            disabled={savingProfile}
                                        >
                                            <X size={17} />
                                            Cancel
                                        </button>
                                    )}

                                    <button
                                        type="submit"
                                        className="profile-primary-button"
                                        disabled={savingProfile}
                                    >
                                        {savingProfile ? (
                                            <LoaderCircle size={17} className="profile-spin" />
                                        ) : (
                                            <Save size={17} />
                                        )}
                                        {savingProfile ? "Saving..." : "Save Profile"}
                                    </button>
                                </div>
                            </form>
                        </section>
                    ) : (
                        <section className="profile-panel">
                            <div className="profile-overview">
                                <div className="profile-avatar">
                                    <UserRound size={34} />
                                </div>

                                <div className="profile-identity">
                                    <h2>{profile.name}</h2>
                                    <p>{profile.headline}</p>
                                    <span className="profile-role-badge">{profile.role}</span>
                                </div>
                            </div>

                            <div className="profile-info-grid">
                                <div className="profile-info-item">
                                    <Mail size={18} />
                                    <div>
                                        <span>Email Address</span>
                                        <strong>{profile.email}</strong>
                                    </div>
                                </div>

                                <div className="profile-info-item">
                                    <Phone size={18} />
                                    <div>
                                        <span>Phone Number</span>
                                        <strong>{profile.phone}</strong>
                                    </div>
                                </div>

                                <div className="profile-info-item">
                                    <MapPin size={18} />
                                    <div>
                                        <span>Location</span>
                                        <strong>{profile.location}</strong>
                                    </div>
                                </div>

                                <div className="profile-info-item">
                                    <BriefcaseBusiness size={18} />
                                    <div>
                                        <span>Account Role</span>
                                        <strong>{profile.role}</strong>
                                    </div>
                                </div>
                            </div>

                            <div className="profile-summary-section">
                                <h3>
                                    <FileText size={18} />
                                    Professional Summary
                                </h3>
                                <p className="profile-summary-text">
                                    {profile.summary || "No professional summary added yet."}
                                </p>
                            </div>
                        </section>
                    )}

                    <section className="profile-panel profile-skills-panel">
                        <div className="profile-panel-heading">
                            <div className="profile-heading-icon">
                                <Code2 size={20} />
                            </div>
                            <div>
                                <h2>Skills</h2>
                                <p>Select the skills you want recruiters to see.</p>
                            </div>
                        </div>

                        {skillsError && (
                            <div className="profile-inline-error">
                                <AlertCircle size={16} />
                                {skillsError}
                            </div>
                        )}

                        {skills.length === 0 ? (
                            <div className="profile-empty-skills">
                                <Code2 size={28} />
                                <p>No skills are currently available.</p>
                            </div>
                        ) : (
                            <>
                                <div className="profile-skill-selection">
                                    {skills.map((skill) => {
                                        const selected = selectedSkillIds.includes(skill.id);

                                        return (
                                            <button
                                                key={skill.id}
                                                type="button"
                                                className={`profile-skill-option ${selected ? "selected" : ""
                                                    }`}
                                                onClick={() => handleSkillToggle(skill.id)}
                                                aria-pressed={selected}
                                            >
                                                {selected ? (
                                                    <CheckCircle2 size={15} />
                                                ) : (
                                                    <Plus size={15} />
                                                )}
                                                {skill.name}
                                            </button>
                                        );
                                    })}
                                </div>

                                <div className="profile-skills-footer">
                                    <span>
                                        {selectedSkillIds.length} skill
                                        {selectedSkillIds.length !== 1 ? "s" : ""} selected
                                    </span>

                                    <button
                                        type="button"
                                        className="profile-primary-button"
                                        onClick={handleSaveSkills}
                                        disabled={savingSkills || !profileExists}
                                    >
                                        {savingSkills ? (
                                            <LoaderCircle size={17} className="profile-spin" />
                                        ) : (
                                            <Save size={17} />
                                        )}
                                        {savingSkills ? "Saving..." : "Save Skills"}
                                    </button>
                                </div>
                            </>
                        )}

                        <div className="profile-saved-skills">
                            <h3>Current Profile Skills</h3>

                            {savedSkills.length > 0 ? (
                                <div className="profile-saved-skill-list">
                                    {savedSkills.map((skill) => (
                                        <span key={skill} className="profile-saved-skill">
                                            {skill}
                                        </span>
                                    ))}
                                </div>
                            ) : (
                                <p className="profile-muted-text">
                                    No skills saved to your profile yet.
                                </p>
                            )}
                        </div>
                    </section>
                </>
            )}
        </div>
    );
}

export default CandidateProfile;