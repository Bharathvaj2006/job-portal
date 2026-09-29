import api from "./api";

export const candidateService = {
    getJobs() {
        return api.get("/jobs");
    },

    getResume() {
        return api.get("/candidate/profile/resume");
    },

    getMyApplications(page = 0, size = 10) {
        return api.get("/applications/my", {
            params: {
                page,
                size,
            },
        });
    },
};

