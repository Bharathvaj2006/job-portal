import api from "./api";

export const candidateProfileService = {
  getMyProfile: async () => {
    const response = await api.get("/candidate/profile");
    return response.data;
  },

  createProfile: async (profileData) => {
    const response = await api.post(
      "/candidate/profile",
      profileData
    );
    return response.data;
  },

  updateProfile: async (profileData) => {
    const response = await api.put(
      "/candidate/profile",
      profileData
    );
    return response.data;
  },

  updateSkills: async (skillIds) => {
    const response = await api.put(
      "/candidate/profile/skills",
      { skillIds }
    );
    return response.data;
  },

  deleteProfile: async () => {
    await api.delete("/candidate/profile");
  },

  getAllSkills: async () => {
    const response = await api.get("/skills");
    return response.data;
  },
};