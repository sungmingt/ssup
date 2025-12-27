import { api } from "./axios";

export const languageApi = {
  getLanguageList: (userId) => api.get(`/languages`),
  getUserLanguages: (userId) => api.get(`/users/${userId}/languages`),
  updateMyLanguages: (formData) => api.put("/users/me/languages", formData),
};
