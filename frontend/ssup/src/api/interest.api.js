import { api } from "./axios";

export const interestApi = {
  getAll: () => api.get("/interests"),
};
