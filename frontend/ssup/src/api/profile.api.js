import { api } from "./axios";

export const profileApi = {
  getMyProfile: () => api.get("/users/me/profile"),
  getUserProfile: (userId) => api.get(`/users/${userId}/profile`),
  createMyProfile: (formData) => api.post("/users/me/profile", formData),
};
