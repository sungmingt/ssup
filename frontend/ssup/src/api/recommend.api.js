import { api } from "./axios";

export const recommendApi = {
  getRecommend: (userId) => api.get(`/recommend/${userId}`),
  getRandomUsers: () => api.get("/recommend/anonymous"),
};
