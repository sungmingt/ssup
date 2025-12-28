import { api } from "./axios";

export const authApi = {
  signUp: (data) => api.post("/auth/signup", data),
  login: (data) => api.post("/auth/login", data),
  logout: () => api.post("/auth/logout"),
  reissue: () => api.get("/auth/reissue"),
  me: () => api.get("/auth/me", { withCredentials: true }),
};
