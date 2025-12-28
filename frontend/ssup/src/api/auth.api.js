import { api } from "./axios";

export const authApi = {
  signUp: (data) => api.post("/auth/signup", data),
  login: (data) => api.post("/auth/login", data),
  me: () => api.get("/auth/me", { withCredentials: true }),
};
