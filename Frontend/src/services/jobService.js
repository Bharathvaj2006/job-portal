
import api from "./api";

export const jobService = {
  getJobs: async (params = {}) => {
    const response = await api.get("/jobs", {
      params: {
        status: "ACTIVE",
        page: 0,
        size: 10,
        sortBy: "createdAt",
        direction: "desc",
        ...params,
      },
    });

    return response.data;
  },

  getJobById: async (jobId) => {
    const response = await api.get(`/jobs/${jobId}`);
    return response.data;
  },
};
