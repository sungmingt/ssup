import { api } from "./axios";

export const matchApi = {
  sendRequest: (formData) => api.post("/matches", formData),
  acceptRequest: (id) => api.post(`/matches/${id}/accept`),
  rejectRequest: (id) => api.post(`/matches/${id}/reject`),
  getMyMatches: () => api.get("/users/me/matches"),
};
