
import api from "./api";

export const applicationService = {
  applyForJob: async (jobId, coverLetter = "") => {
    const response = await api.post(`/applications/${jobId}`, {
      coverLetter,
    });

    return response.data;
  },

  getMyApplications: async (params = {}) => {
    const response = await api.get("/applications/my", {
      params,
    });

    return response.data;
  },

  withdrawApplication: async (applicationId) => {
    const response = await api.delete(`/applications/${applicationId}`);
    return response.data;
  },
};
