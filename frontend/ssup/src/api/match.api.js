import { api } from "./axios";

export const matchApi = {
  sendRequest: (formData) => api.post("/matches", formData),
  acceptRequest: (id) => api.put(`/matches/${id}/accept`),
  rejectRequest: (id) => api.put(`/matches/${id}/reject`),
  getMyMatches: () => api.get("/users/me/matches"),
};
