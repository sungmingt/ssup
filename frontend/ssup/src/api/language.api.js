import { api } from "./axios";

export const languageApi = {
  getUserLanguages: (userId) => api.get(`/users/${userId}/languages`),
  updateUserLanguages: () => api.put("/users/me/languages"),
};
