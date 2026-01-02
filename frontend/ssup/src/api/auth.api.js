import { api } from "./axios";

export const authApi = {
  signUp: (data) => api.post("/auth/signup", data),
  login: (data) => api.post("/auth/login", data),
  logout: () => api.post("/auth/logout"),
  quit: () => api.post("/auth/quit"),
  reissue: () => api.get("/auth/reissue"),
  me: async () => {
    try {
      return await api.get("/auth/me", { withCredentials: true });
    } catch {
      return null; //여기서 에러 방지
    }
  },
};
